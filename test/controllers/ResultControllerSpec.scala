/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers

import controllers.actions._
import forms.DeclarationFormProvider
import play.api.i18n.Messages
import play.api.test.Helpers._
import viewmodels.AnswerSection
import views.html.results.{FutureSubstitutionView, OfficeHolderEmployedView, OfficeHolderInsideIR35View}

class ResultControllerSpec extends ControllerSpecBase {

  val formProvider = new DeclarationFormProvider()
  val form = formProvider()

  val officeHolderInsideIR35View = injector.instanceOf[OfficeHolderInsideIR35View]
  val officeHolderEmployedView = injector.instanceOf[OfficeHolderEmployedView]
  val futureSubstitutionView = injector.instanceOf[FutureSubstitutionView]

  val postAction = routes.ResultController.onSubmit() //TODO: this will need to go to the PDF controller

  val answers = Seq(
    AnswerSection(Some(Messages("result.peopleInvolved.h2")), Seq()),
    AnswerSection(Some(Messages("result.workersDuties.h2")), Seq()),
    AnswerSection(Some(Messages("result.substitutesHelpers.h2")), Seq()),
    AnswerSection(Some(Messages("result.workArrangements.h2")), Seq()),
    AnswerSection(Some(Messages("result.financialRisk.h2")), Seq()),
    AnswerSection(Some(Messages("result.partAndParcel.h2")), Seq())
  )

  val version = "1.5.0-final"

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new ResultController(
    frontendAppConfig,
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    officeHolderInsideIR35View,
    officeHolderEmployedView,
    futureSubstitutionView,
    formProvider
  )

  def viewAsString() = officeHolderInsideIR35View(frontendAppConfig, answers, version, form, postAction)(fakeRequest, messages).toString

  //TODO: Currently only renders the one view; this will need to cater for all views
  "ResultPage Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }
  }
}




