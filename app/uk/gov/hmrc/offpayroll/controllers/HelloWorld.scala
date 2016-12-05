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
import uk.gov.hmrc.offpayroll.OffPayrollWebflow


object HelloWorld extends HelloWorld

trait HelloWorld extends FrontendController {


  def begin(clusterID: Int) = Action.async { implicit request =>
    //get the first question page from the webflow

    // @TODO need to deal with zero indexing
    val element = OffPayrollWebflow.clusters(clusterID).clusterElements(0)

    val userForm = Form (
      single(
        element.questionTag -> boolean
      )
    )

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.helloworld.begin(userForm,element)))
  }

  def getElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>
    //get the first question page from the webflow

    val element = OffPayrollWebflow.clusters(clusterID).clusterElements(elementID)

    val userForm = Form (
      single(
        element.questionTag -> boolean
      )
    )

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.helloworld.begin(userForm,element)))
  }

  def processElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>
    //get the first question page from the webflow

    val element = OffPayrollWebflow.clusters(clusterID).clusterElements(elementID)

    val userForm = Form (
      single(
        element.questionTag -> boolean
      )
    )

    Future.successful(userForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(uk.gov.hmrc.offpayroll.views.html.helloworld.begin(formWithErrors, element))
      },
      value => {
        /* Hardcode of the next element here this will be dynamic */
        Redirect(uk.gov.hmrc.offpayroll.controllers.routes.HelloWorld.getElement(clusterID, elementID +1))
          .flashing(request.flash + (element.questionTag -> String.valueOf(value)))
          .withSession(request.session + (element.questionTag -> String.valueOf(value)))

      }
    ))
  }



  val stepSuccess = Action.async { implicit request =>
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.helloworld.step_success()))
  }


}
