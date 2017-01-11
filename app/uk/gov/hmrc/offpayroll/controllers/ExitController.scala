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
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models.{Decision, Element, ExitFlow, MULTI}
import uk.gov.hmrc.offpayroll.services.{FlowService, FragmentService, IR35FlowService}
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future

/**
  * Created by peter on 09/01/2017.
  */


object ExitController {
  def apply = new ExitController
}

class ExitController  @Inject() extends FrontendController  with OffPayrollControllerHelper {

  val fragmentService = FragmentService("/guidance/")
  val flow = ExitFlow

  def begin = Action.async { implicit request =>

    val element = flow.getStart()

    val questionForm = createForm(element)

    implicit val session: Map[String, String] = request.session.data

    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.setup(questionForm,element,
      fragmentService.getFragmentByName(element.questionTag))))
  }

}
