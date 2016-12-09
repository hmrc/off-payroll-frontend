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
import uk.gov.hmrc.offpayroll.views.html.interview.element


object InterviewController extends InterviewController

trait InterviewController extends FrontendController {

  val webflow: Webflow = OffPayrollWebflow


  def begin(clusterID: Int) = Action.async { implicit request =>
    //get the first question page from the webflow

    val element = webflow.getStart()

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

  def getElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>
    //get the first question page from the webflow

    implicit val session: Map[String, String] = request.session.data

    val element: Option[Element] = webflow.getEelmentById(clusterID, elementID)

    if (element.nonEmpty) {
      val tag = element.head.questionTag
      val userForm = Form(
        single(
          tag -> boolean
        )
      )

      Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.element(userForm, element.head)))
    }
    else {

      val mockDecision = Decision(session, IN)
      Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(mockDecision)))
    }

  }

  def processElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>

    val element: Option[Element] = webflow.getEelmentById(clusterID, elementID)
    val tag: String = element.head.questionTag

    // @TODO must validate expected tags are populated
    val singleForm = Form(
      single(
        tag -> boolean
      )
    )

    Logger.debug(" ************ Request *****************:  " + request.body)
    Logger.debug(" ************ tag *****************:  " + tag)

    implicit val session: Map[String, String] = request.session.data

    Future.successful(
      singleForm.bindFromRequest.fold(
        formWithErrors => {
          Logger.debug("  *********** Bad Request  *********** ")
          // binding failure, you retrieve the form containing errors:
          BadRequest(uk.gov.hmrc.offpayroll.views.html.interview.element(formWithErrors, element.head))
        },
        value => {
          Logger.debug(" *********** Value **************: " + value)
          /* Hardcode of the next element here this will be dynamic */
          Redirect(uk.gov.hmrc.offpayroll.controllers.routes.InterviewController.getElement(clusterID, elementID + 1))
            .withSession(request.session + (tag -> String.valueOf(value)))

        }
      )

    )
  }


  //  val stepSuccess = Action.async { implicit request =>
  //    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.helloworld.step_success()))
  //  }


}
