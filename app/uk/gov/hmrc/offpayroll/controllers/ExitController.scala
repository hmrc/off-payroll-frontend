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

import play.Logger
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{ExitFlow, ExitReason}
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.{addValue, asMap}

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


  def begin() = PasscodeAuthenticatedActionAsync { implicit request =>

    val element = flow.getStart()

    val questionForm = createForm(element)

//    implicit val session: Map[String, String] = request.session.data

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.exit(questionForm,element,
      fragmentService.getFragmentByName(element.questionTag))))
  }



  def processElement(elementID: Int) = PasscodeAuthenticatedActionAsync { implicit request =>

    val element = flow.getElementById(EXIT_CLUSTER_ID, elementID).get
    val fieldName = element.questionTag
    val form = createForm(element)
//    implicit val session: Map[String, String] = request.session.data

    form.bindFromRequest.fold (
      formWithErrors =>
        Future.successful(BadRequest(
          uk.gov.hmrc.offpayroll.views.html.interview.setup(
            formWithErrors, element, Html.apply("")))),

      value => {
//        implicit val session: Map[String, String] = request.session.data + (fieldName -> value)
        val session = addValue(request.session, fieldName, value)

        val exitResult = flow.shouldAskForNext(asMap(session), (fieldName, value))

        if(exitResult.element.nonEmpty) { // continue with questions
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.exit(form, exitResult.element.get,
            fragmentService.getFragmentByName(exitResult.element.get.questionTag)))
            .withSession(session)
          )

        } else if(exitResult.inIr35) { // in IR35
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.hardDecision()))
        } else if(exitResult.exitTool) { // exit the tool
          val exitReason = ExitReason("exitTool.serviceProvision.heading","exitTool.serviceProvision.reason","exitTool.serviceProvision.explanation")
          Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.exitTool(exitReason)))
        } else if(exitResult.continueToMainInterview) {
          Future.successful(Redirect(routes.InterviewController.begin)
            .withSession(session))
        } else { // bad
          Future.successful(InternalServerError("Unknown result from the ExitFlow"))
        }
      }
    )
  }
}
