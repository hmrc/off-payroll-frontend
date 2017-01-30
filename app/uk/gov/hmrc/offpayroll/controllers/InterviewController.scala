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

import java.util.NoSuchElementException
import javax.inject.Inject

import play.api.Logger
import play.api.Play._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.filters.SessionIdFilter._
import uk.gov.hmrc.offpayroll.models.{Element, Webflow}
import uk.gov.hmrc.offpayroll.services.{FlowService, IR35FlowService}
import uk.gov.hmrc.passcode.authentication.{PasscodeAuthentication, PasscodeAuthenticationProvider, PasscodeVerificationConfig}
import play.api.i18n.Messages.Implicits._
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.{asMap, push}

import scala.concurrent.Future


trait OffPayrollControllerHelper extends PasscodeAuthentication  {
  //@Todo write tests


  override def config = new PasscodeVerificationConfig(configuration)
  override def passcodeAuthenticationProvider = new PasscodeAuthenticationProvider(config)


  /**
    * Create a Form based on an Element
    * @param element
    * @return
    */
  def createForm(element: Element): Form[String] = {
    createForm(element.questionTag)
  }

  /**
    * Convience overload to alow form creation by the string name of the element
    * @param element
    * @return
    */
  def createForm(element: String): Form[String] ={
    Form(
    single(
    element -> nonEmptyText
    )
    )
  }

  def yesNo(value: Boolean): String =
  if(value) "Yes" else "No"

}

class SessionHelper {
  def createCorrelationId(request:Request[_]) =
  request.cookies.get(OPF_SESSION_ID_COOKIE).map(_.value) match {
    case None => throw new NoSuchElementException("session id not found in the cookie")
    case Some(value) => value
  }
}

object InterviewController {
  def apply() = {
    new InterviewController(IR35FlowService(), new SessionHelper)
  }
}

class InterviewController @Inject()(val flowService: FlowService, val sessionHelper: SessionHelper) extends OffPayrollController {


  val flow: Webflow = flowService.flow

  def begin = PasscodeAuthenticatedActionAsync { implicit request =>

    val element = flowService.getStart()
    val form = createForm(element)

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(form, element,
    fragmentService.getFragmentByName(element.questionTag))))
  }

  override def displaySuccess(element: Element, questionForm: Form[String])(html: Html)(implicit request: Request[_]): Result =
    Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(questionForm, element, html))

  override def redirect: Result = Redirect(routes.ExitController.back)

  def processElement(clusterID: Int, elementID: Int) = PasscodeAuthenticatedActionAsync { implicit request =>

    val element = flowService.getAbsoluteElement(clusterID, elementID)
    val fieldName = element.questionTag
    val form = createForm(element)

    form.bindFromRequest.fold (
    formWithErrors =>
    Future.successful(BadRequest(
    uk.gov.hmrc.offpayroll.views.html.interview.interview(
    formWithErrors, element, Html.apply(element.questionTag)))),

    value => {
          val session = push(request.session, fieldName, value)
          val result = flowService.evaluateInterview(asMap(session), (fieldName, value), sessionHelper.createCorrelationId(request))

          result.map(
          decision => {
              if (decision.continueWithQuestions) {
                Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(
                form, decision.element.head, fragmentService.getFragmentByName(decision.element.head.questionTag)))
                  .withSession(session)
              } else {
                Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decision.decision.head))
              }
            }
          )
        }
    )
  }


}
