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

import javax.inject.Inject

import play.api.{Logger, Play}
import play.api.Play.current
import play.api.data.Form
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{Element, ExitFlow, ExitReason, PersonalServiceCluster}
import uk.gov.hmrc.offpayroll.services.IR35FlowService
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack.{asMap, pop, push}

import scala.concurrent.Future

/**
  * Created by peter on 09/01/2017.
  */


object ExitController {
  def apply = new ExitController
}

class ExitController  @Inject() extends OffPayrollController {

  val flow = ExitFlow
  val EXIT_CLUSTER_ID: Int = 0
  @Inject var interviewController: InterviewController = null


  def beginSuccess(element: Element)(implicit request: Request[AnyContent]) = {

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.exit(emptyForm, element,
      fragmentService.getFragmentByName(element.questionTag))))
  }

  override def displaySuccess(element: Element, questionForm: Form[_])(html: Html)(implicit request: Request[_]) = {
    Ok(uk.gov.hmrc.offpayroll.views.html.interview.exit(questionForm, element, html))
  }

  override def redirect = Redirect(routes.SetupController.back)


  def processElement(elementID: Int) = Action.async { implicit request =>
    val maybeElement = flow.getElementById(EXIT_CLUSTER_ID, elementID)
    checkElementIndex("exit", maybeElement)(doProcessElement)
  }

  private def doProcessElement(element: Element)(implicit request: Request[AnyContent]): Future[Result] = {
    val fieldName = element.questionTag
    val form = createForm(element)

    form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(
          uk.gov.hmrc.offpayroll.views.html.interview.exit(
            formWithErrors, element, fragmentService.getFragmentByName(element.questionTag))))
      },

      value => {
        val session = push(request.session, value, element)
        val inIr35 = flow.shouldAskForNext(asMap(session), (fieldName, value)).inIr35

        if (inIr35) {
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.hardDecision()))
        } else {
          Future.successful(Redirect(routes.InterviewController.begin).
            withSession(InterviewSessionStack.addCurrentIndex(session, interviewController.startElement)))
        }
      }
    )
  }
}

