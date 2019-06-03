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

package views.sections.personalService

import assets.messages.WouldPaySubstituteMessages
import config.SessionKeys
import controllers.sections.personalService.routes
import forms.WouldWorkerPaySubstituteFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.subOptimised.sections.personalService.WouldWorkerPaySubstituteView

class WouldWorkerPaySubstituteViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "wouldWorkerPaySubstitute"

  val form = new WouldWorkerPaySubstituteFormProvider()()

  val view = injector.instanceOf[WouldWorkerPaySubstituteView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WouldWorkerPaySubstitute view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.WouldWorkerPaySubstituteController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.Worker.title, Some(WouldPaySubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe WouldPaySubstituteMessages.subheading
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe WouldPaySubstituteMessages.Worker.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.Hirer.title, Some(WouldPaySubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe WouldPaySubstituteMessages.subheading
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe WouldPaySubstituteMessages.Hirer.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(WouldPaySubstituteMessages.NonTailored.title, Some(WouldPaySubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WouldPaySubstituteMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe WouldPaySubstituteMessages.subheading
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe WouldPaySubstituteMessages.NonTailored.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WouldPaySubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WouldPaySubstituteMessages.no
      }
    }
  }
}
