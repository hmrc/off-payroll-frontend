/*
 * Copyright 2016 HM Revenue & Customs
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

import uk.gov.hmrc.play.frontend.controller.FrontendController
import play.api.mvc._

import scala.concurrent.Future
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import uk.gov.hmrc.offpayroll._
import play.api.Logger
import uk.gov.hmrc.offpayroll.models.Decision
import uk.gov.hmrc.offpayroll.service.IR35FlowService


object InterviewController extends InterviewController

trait InterviewController extends FrontendController {

  val flowService = IR35FlowService

  def begin(clusterID: Int) = Action.async { implicit request =>

    val element = flowService.getStart()

    val userForm = Form(
      single(
        element.questionTag -> boolean
      )
    )
    implicit val session: Map[String, String] = request.session.data
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.element(userForm, element)))
  }

  def displayDecision(decsion: Decision) = Action.async { implicit request =>
    implicit val session: Map[String, String] = request.session.data
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decsion)))
  }

  private def yesNo(value: Boolean): String =
    if(value) "Yes" else "No"

  def processElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>

    val element = flowService.getCurrent(clusterID, elementID)
    val tag: String = element.questionTag

    val singleForm = Form(
      single( // @TODO must validate expected tags are populated
        tag -> boolean
      )
    )
    Logger.debug(" *** Request + tag ***:  " + request.body + " " + tag)

    implicit val session: Map[String, String] = request.session.data

      singleForm.bindFromRequest.fold (
        formWithErrors =>
          Future.successful(BadRequest(uk.gov.hmrc.offpayroll.views.html.interview.element(formWithErrors, element))),

        value => {
          Logger.debug(" *********** Value **************: " + value)
          implicit val session: Map[String, String] = request.session.data + (tag -> yesNo(value))

          val result = flowService.evaluateInterview(session, (tag, yesNo(value)))

          result.map(
            decision => {
              if (decision.continueWithQuestions) {
                Ok(uk.gov.hmrc.offpayroll.views.html.interview.element(singleForm, decision.element.head))
                  .withSession(request.session + (tag -> yesNo(value)))
              } else {
                Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decision.decision.head))
              }
            }
          )
        }
      )
  }


}
