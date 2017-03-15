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

import uk.gov.hmrc.offpayroll.services.FragmentService
import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.Logger
import play.api.data.Form
import play.api.data.Forms.single
import play.api.mvc.{Request, Result}
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{Element, Webflow}
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack.{asMap, peek, pop}
import play.api.data.Forms.text
import play.api.mvc._
import uk.gov.hmrc.offpayroll.util.{ElementProvider, InterviewSessionStack}
import play.api.Play._
import play.api.i18n.Messages.Implicits._

import scala.concurrent.Future

/**
  * Created by peter on 11/01/2017.
  */
abstract class OffPayrollController extends FrontendController  with OffPayrollControllerHelper {

  val fragmentService = FragmentService("/guidance/")

  val flow:Webflow

  def displaySuccess(element: Element, questionForm: Form[_])(html: Html)(implicit request: Request[_]): Result

  def redirect: Result

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

  def begin() = Action.async { implicit request =>
    maybeStartElement.fold (
      Future.successful(Redirect(routes.SetupController.begin).withSession(request.session))
    ) (
      beginSuccess(_)
    )
  }

  private def maybeStartElement(implicit request: Request[AnyContent]) = flow.getStart(asMap(request.session))

  def startElement(implicit request: Request[AnyContent]) = maybeStartElement.getOrElse(ElementProvider.toElements(0))

  def beginSuccess(element: Element)(implicit request:Request[AnyContent]): Future[Result]

  val emptyForm = Form(single("" -> text))

  def checkElementIndex(message: String, maybeElement: Option[Element])(f: Element => Future[Result])(implicit request: Request[_]): Future[Result] = {
    val indexElement = InterviewSessionStack.currentIndex(request.session)
    maybeElement.fold {
      Logger.error("could not find index in session, redirecting to the start")
      Future.successful(Redirect(routes.SetupController.begin))
      }{ element =>
      if (element != indexElement) {
        Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.multipleInterview()))
      }
      else {
        f(element)
      }
    }
  }
}
