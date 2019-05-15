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

package views.results

import akka.http.scaladsl.model.HttpMethods
import assets.messages.ArrangedSubstituteMessages
import config.SessionKeys
import controllers.routes
import forms.DeclarationFormProvider
import models.AboutYouAnswer.Worker
import models.CannotClaimAsExpense.WorkerUsedVehicle
import models.{AdditionalPdfDetails, CheckMode, Timestamp}
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}
import views.behaviours.ViewBehaviours
import views.html.results.OfficeHolderInsideIR35View

class OfficeHolderInsideIR35ViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "result.officeHolderInsideIR35"

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[OfficeHolderInsideIR35View]

  val postAction = Call(HttpMethods.POST.value, "/")

  val answers = Seq(
    AnswerSection(Some(Messages("result.workersDuties.h2")), whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))), Seq(
      (AnswerRow(
        label = "contractStarted.checkYourAnswersLabel",
        answer = "site.yes",
        answerIsMessageKey = true,
        changeUrl = routes.ContractStartedController.onPageLoad(CheckMode).url
      ),None)
    ))
  )

  val version = "1.0"

  val timestamp = Timestamp.timestamp

  def createView = () => view(answers, version, form, postAction)(fakeRequest, messages, frontendAppConfig)

  val model = AdditionalPdfDetails(Some("Gerald"), Some("PBPlumbin"), Some("Plumber"), Some("Boiler man"))

  def createPrintView = () => view(answers, version, form, postAction, true, Some(model), Some(timestamp))(fakeRequest, messages, frontendAppConfig)

  "ResultPrintPage view" must {
    behave like printPage(createPrintView, model, timestamp, messageKeyPrefix)
  }

  "ResultPage view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }

  "If the user type is of Worker" should {

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
    lazy val document = asDocument(createViewWithRequest(request))

    "have the correct label" in {
      document.select(Selectors.p(1)).text mustBe ArrangedSubstituteMessages.Worker.p1
    }
  }
}
