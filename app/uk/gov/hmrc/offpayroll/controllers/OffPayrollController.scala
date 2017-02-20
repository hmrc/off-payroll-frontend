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
import uk.gov.hmrc.offpayroll.models.{Element, ExitFlow, Webflow}
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.{peek, pop}
import play.api.data.Forms.text
import play.api.mvc._

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
        (fragmentService.getFragmentByName(element.questionTag)).withSession(session))
      }
      case None => Future.successful(redirect.withSession(peekSession))
    }
  }

  val emptyForm = Form(single("" -> text))
}
