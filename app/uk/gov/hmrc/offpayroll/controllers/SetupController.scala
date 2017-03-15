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

import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms.{single, _}
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, Request, Result, AnyContent}
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{Element, ExitReason, SetupFlow}
import uk.gov.hmrc.offpayroll.util.InterviewSessionStack.{reset, push, asMap, addCurrentIndex}

import scala.concurrent.Future


object SetupController {
  def apply = new SetupController
}


/**
  * Created by peter on 09/01/2017.
  */
class SetupController @Inject() extends OffPayrollController {

  val flow = SetupFlow
  val SETUP_CLUSTER_ID = 0
  val exitController = ExitController.apply

  override def beginSuccess(element: Element)(implicit request: Request[AnyContent]): Future[Result] = {

    val session = reset(request.session)
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.setup(emptyForm, element,
      fragmentService.getFragmentByName(element.questionTag))).withSession(addCurrentIndex(session,element)))
  }

  override def displaySuccess(element: Element, questionForm: Form[_])(html: Html)(implicit request: Request[_]): Result =
    Ok(uk.gov.hmrc.offpayroll.views.html.interview.setup(questionForm, element, html))

  override def redirect: Result = Redirect(routes.SetupController.begin())


  def processElement(elementID: Int) = Action.async { implicit request =>
    val maybeElement = flow.getElementById(SETUP_CLUSTER_ID, elementID).orElse(flow.getStart(asMap(request.session)))
    checkElementIndex("setup", maybeElement)(doProcessElement)
  }

  private def doProcessElement(element: Element)(implicit request: Request[AnyContent]): Future[Result] = {
    val fieldName = element.questionTag
    val form = createForm(element)

    form.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(
          uk.gov.hmrc.offpayroll.views.html.interview.setup(
            formWithErrors, element, fragmentService.getFragmentByName(element.questionTag))))
      },

      value => {
        val session = push(request.session, value, element)

        val setupResult = flow.shouldAskForNext(asMap(session), (fieldName, value))
        if (setupResult.maybeElement.nonEmpty) {
          // continue setup
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.setup(form, setupResult.maybeElement.get,
            fragmentService.getFragmentByName(setupResult.maybeElement.get.questionTag)))
            .withSession(addCurrentIndex(session, setupResult.maybeElement.get))
          )
        } else if (setupResult.exitTool) {
          val exitReason = ExitReason("exitTool.soleTrader.heading", "exitTool.soleTrader.reason", "exitTool.soleTrader.explanation")
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.exitTool(exitReason))
            .withSession(session))
        }
        else {
          // ExitCluster
          Future.successful(Redirect(routes.ExitController.begin()).withSession(addCurrentIndex(session, exitController.startElement)))
        }
      }
    )
  }
}
