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
import uk.gov.hmrc.offpayroll.models.{Decision, Element, MULTI}
import uk.gov.hmrc.offpayroll.services.{FlowService, FragmentService, IR35FlowService}
import uk.gov.hmrc.play.frontend.controller.FrontendController

import scala.concurrent.Future


trait OffPayrollControllerHelper {
  //@Todo write tests

  /**
    * Create a Form based on an Element
    * @param element
    * @return
    */
  def createForm(element: Element): Form[Boolean] = {
    createForm(element.questionTag)
  }

  /**
    * Convience overload to alow form creation by the string name of the element
    * @param element
    * @return
    */
  def createForm(element: String): Form[Boolean] ={
    Form(
      single(
        element -> boolean
      )
    )
  }

  /**
    * Creates a Form for a ChildElement when we don't know until Runtime which of the children will be passed
    * @param element
    * @param requestBody
    * @return
    */
  def createForm(element: Element, requestBody: Option[Map[String, Seq[String]]]): (String, Form[Boolean]) = {

    if(element.elementType == MULTI) {
      if (requestBody.nonEmpty) {
       val fieldName =  requestBody.get.find{
          case(name,value) => element.children.exists(e => e.questionTag == name)
        }.get
        (fieldName._1, createForm(fieldName._1))
      } else throw new IllegalArgumentException("Form body was empty")
    } else (element.questionTag, createForm(element))
  }

  def yesNo(value: Boolean): String =
    if(value) "Yes" else "No"

}


object InterviewController {

  def apply() = {
    new InterviewController(IR35FlowService())
  }
}

class InterviewController @Inject()(val flowService: FlowService) extends FrontendController with OffPayrollControllerHelper {

  val fragmentService = FragmentService("/guidance/")

  def begin(clusterID: Int) = Action.async { implicit request =>

    val element = flowService.getStart()

    val form: Form[Boolean] = createForm(element)

    implicit val session: Map[String, String] = request.session.data
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(form, element,
      fragmentService.getFragmentByName(element.questionTag))))
  }



  def displayDecision(decsion: Decision) = Action.async { implicit request =>
    implicit val session: Map[String, String] = request.session.data
    Future.successful(Ok(uk.gov.hmrc.offpayroll.views.html.interview.display_decision(decsion)))
  }



  def processElement(clusterID: Int, elementID: Int) = Action.async { implicit request =>

    val element = flowService.getAbsoluteElement(clusterID, elementID)
    val (tag,form) = createForm(element, request.body.asFormUrlEncoded)

    implicit val session: Map[String, String] = request.session.data

      form.bindFromRequest.fold (
        formWithErrors =>
          Future.successful(BadRequest(
            uk.gov.hmrc.offpayroll.views.html.interview.interview(
              formWithErrors, element, Html.apply("")))),

        value => {
          implicit val session: Map[String, String] = request.session.data + (tag -> yesNo(value))

          val result = flowService.evaluateInterview(session, (tag, yesNo(value)))

          result.map(
            decision => {
              if (decision.continueWithQuestions) {
                Ok(uk.gov.hmrc.offpayroll.views.html.interview.interview(
                  form, decision.element.head, fragmentService.getFragmentByName(decision.element.head.questionTag)))
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
