/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.offpayroll.controllers

import java.util.{NoSuchElementException, UUID}
import javax.inject.Inject

import play.Logger
import play.api.Play._
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.Constraint
import play.api.i18n.{Lang, Messages}
import play.api.i18n.Messages.Implicits._
import play.api.libs.json.{Format, Json}
import play.api.mvc.Results.Ok
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.filters.SessionIdFilter._
import uk.gov.hmrc.offpayroll.models._
import uk.gov.hmrc.offpayroll.services.{FlowService, FragmentService, IR35FlowService}
import uk.gov.hmrc.offpayroll.util.{ElementProvider, InterviewSessionHelper, InterviewSessionStack, ResultPageHelper}
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack._
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future


trait OffPayrollControllerHelper {

  def verifyElement(element: Element)(): Constraint[List[String]] = Constraint[List[String]]("constraint.required") {
    element.verify(_)
  }

  def createForm(element: Element) = {
    Form(
      single(
        element.questionTag -> nonEmptyText
      )
    )
  }

  def createListForm(element: Element) = {
    val verify = verifyElement(element)
    Form(
      single(
        element.questionTag -> list(nonEmptyText).verifying(verify)
      )
    )
  }

  def yesNo(value: Boolean): String =
    if (value) "Yes" else "No"

}

class SessionHelper {
  def getCorrelationId(request: Request[_]) = request.cookies.get(OPF_SESSION_ID_COOKIE)
}

object InterviewController {
  def apply() = {
    new InterviewController(IR35FlowService(), new SessionHelper)
  }
}

class InterviewController @Inject()(val flowService: FlowService, val sessionHelper: SessionHelper) extends OffpayrollController {

  val flow: OffPayrollWebflow = flowService.flow

  private def displaySuccess(element: Element, questionForm: Form[_])(html: Html)(implicit request: Request[_]): Result =
    Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(questionForm, element, html))

  private def redirect: Result = Redirect(routes.InterviewController.begin)

  def back = Action.async { implicit request =>

    val (peekSession, peekQuestionTag) = peek(request.session)

    flow.getElementByTag(peekQuestionTag) match {
      case Some(element) => {
        val (session, questionTag) = pop(request.session)
        Future.successful(displaySuccess(element, emptyForm)
        (fragmentService.getFragmentByName(element.questionTag)).withSession(InterviewSessionStack.addCurrentIndex(session, element)))
      }
      case None => Future.successful(redirect.withSession(peekSession))
    }
  }

  private val ofpsessionid = "ofpSessionId"

  def begin() = Action.async { implicit request =>

    val maybeStartElement =  flow.getStart(asMap(request.session))

    maybeStartElement.fold (
      Future.successful(Redirect(routes.InterviewController.begin).withSession(request.session))
    ) (
      beginSuccess(_)
    )
  }

  private def beginSuccess(element: Element)(implicit request: Request[AnyContent]): Future[Result] = {
    val session = reset(request.session)
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(emptyForm, element,
      fragmentService.getFragmentByName(element.questionTag))).withSession(addCurrentIndex(session,element)))
  }

  val emptyForm = Form(single("" -> text))

  def checkElementIndex(message: String, maybeElement: Option[Element])(f: Element => Future[Result])(implicit request: Request[_]): Future[Result] = {
    val indexElement = InterviewSessionStack.currentIndex(request.session)
    def start: Future[Result] = {
      Future.successful(Redirect(routes.InterviewController.begin))
    }
    maybeElement.fold {
      Logger.error("could not find index in session, redirecting to the start")
      start
    }{ element =>
      if (element != indexElement) {
        start
      }
      else {
        f(element)
      }
    }
  }


  def processElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>
    val element = flowService.getAbsoluteElement(clusterID, elementID)
    checkElementIndex("interview", Some(element))(doProcessElement)
  }

  private def doProcessElement(element: Element)(implicit request: Request[AnyContent]): Future[Result] = {
    val fieldName = element.questionTag

    element.elementType match {
      case GROUP => {
        val newForm = createListForm(element).bindFromRequest
        newForm.fold(
          formWithErrors => handleFormError(element, fieldName, newForm, formWithErrors),
          value => {
            evaluateInteview(element, fieldName, value.mkString("|", "|", ""), newForm)
          }
        )

      }

      case _ => {
        val newForm = createForm(element).bindFromRequest
        newForm.fold(
          formWithErrors => handleFormError(element, fieldName, newForm, formWithErrors),
          value => {
            evaluateInteview(element, fieldName, value, newForm)
          }
        )

      }
    }

  }

  private def handleFormError(element: Element, fieldName: String, newForm: Form[_], formWithErrors: Form[_])(implicit request : play.api.mvc.Request[_]) = {
    Logger.debug("****************** " + fieldName + " " + newForm.data.mkString("~"))
    Future.successful(BadRequest(
      uk.gov.hmrc.offpayroll.views.html.interview.interview(
        formWithErrors, element, fragmentService.getFragmentByName(element.questionTag))))
  }

  private def evaluateInteview(element: Element, fieldName: String, formValue: String, form: Form[_])(implicit request : play.api.mvc.Request[_]) = {
    Logger.debug("****************** " + fieldName + " " + form.data.toString() + " " + formValue)
    sessionHelper.getCorrelationId(request).map(_.value) match {
      case None => {
        Logger.info(s"user has cookies disabled: user agent: ${request.headers.toMap.get("User-Agent")}")
        Logger.info(s"user has cookies disabled: interview: ${asRawList(request.session)}")
        Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.cookiesDisabled()))
      }
      case Some(value) => {
        val session = push(request.session, formValue, element)
        val result = flowService.evaluateInterview(asMap(session), (fieldName, formValue), value, session.get("interview").getOrElse(""))

        result.map(
          interviewEvaluation => {
            if (interviewEvaluation.continueWithQuestions) {
              Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(
                form, interviewEvaluation.element.head, fragmentService.getFragmentByName(interviewEvaluation.element.head.questionTag)))
                .withSession(InterviewSessionStack.addCurrentIndex(session, interviewEvaluation.element.head))
            } else {
              val compressedInterview = logResponse(interviewEvaluation.decision, session, value)
              val fragments = fragmentService.getAllFragmentsForInterview(asMap(session)) ++ fragmentService.getFragmentsByFilenamePrefix("result")
              val isEsi = esi(asMap(session))
              val resultPageHelper = ResultPageHelper(asRawList(session), interviewEvaluation.decision.map(_.decision).getOrElse(UNKNOWN),
                fragments, interviewEvaluation.decision.map(_.cluster).getOrElse("unknownCluster"), isEsi)
              Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(interviewEvaluation.decision.head,
                asRawList(session), isEsi, compressedInterview, resultPageHelper, emptyForm))
                .withSession(InterviewSessionStack.addCurrentIndex(session, ElementProvider.toElements(0)))
            }
          }
        )
      }
    }
  }

  private def esi(interview: Map[String, String]): Boolean = {
    interview.exists{
        case (question, answer) => "setup.provideServices.soleTrader" == answer
      }
  }

  private def logResponse(maybeDecision: Option[Decision], session: Session, correlationId: String): String =
    session.get("interview").fold{Logger.error("interview is empty")
      ""} { compressedInterview =>
      val esiOrIr35Route = if (esi(asMap(session))) "ESI" else "IR35"
      val version = maybeDecision.map(_.version).getOrElse("unknown")
      val decision = maybeDecision.map(_.decision).getOrElse("decision is not known").toString
      val responseBody = Json.toJson(DecisionResponse(compressedInterview, esiOrIr35Route, version, correlationId, decision))
      Logger.info(s"DECISION: ${responseBody.toString.replaceAll("\"", "")}")
      compressedInterview
    }
}

case class DecisionResponse(interview:String, esiOrIr35Route: String,version:String, correlationID:String, decision: String)

object DecisionResponse {
  implicit val decisionResponseFormat: Format[DecisionResponse] = Json.format[DecisionResponse]
}