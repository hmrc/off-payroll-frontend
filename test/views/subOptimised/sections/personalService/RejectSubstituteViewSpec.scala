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

package views.subOptimised.sections.personalService

import assets.messages.RejectSubstituteMessages
import config.SessionKeys
import controllers.sections.personalService.routes
import forms.RejectSubstituteFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.subOptimised.sections.personalService.RejectSubstituteView

class RejectSubstituteViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "rejectSubstitute"

  val form = new RejectSubstituteFormProvider()(frontendAppConfig)()

  val view = injector.instanceOf[RejectSubstituteView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "RejectSubstitute view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.RejectSubstituteController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Worker.title, Some(RejectSubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe RejectSubstituteMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Worker.p1
        document.select(Selectors.bullet(1)).text mustBe RejectSubstituteMessages.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe RejectSubstituteMessages.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe RejectSubstituteMessages.Worker.b3
        document.select(Selectors.bullet(4)).text mustBe RejectSubstituteMessages.Worker.b4
        document.select(Selectors.bullet(5)).text mustBe RejectSubstituteMessages.Worker.b5
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe RejectSubstituteMessages.Worker.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.Worker.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.Worker.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Hirer.title, Some(RejectSubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe RejectSubstituteMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Hirer.p1
        document.select(Selectors.bullet(1)).text mustBe RejectSubstituteMessages.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe RejectSubstituteMessages.Hirer.b2
        document.select(Selectors.bullet(3)).text mustBe RejectSubstituteMessages.Hirer.b3
        document.select(Selectors.bullet(4)).text mustBe RejectSubstituteMessages.Hirer.b4
        document.select(Selectors.bullet(5)).text mustBe RejectSubstituteMessages.Hirer.b5
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe RejectSubstituteMessages.Hirer.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.Hirer.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.Hirer.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.NonTailored.title, Some(RejectSubstituteMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe RejectSubstituteMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.NonTailored.p1
        document.select(Selectors.bullet(1)).text mustBe RejectSubstituteMessages.NonTailored.b1
        document.select(Selectors.bullet(2)).text mustBe RejectSubstituteMessages.NonTailored.b2
        document.select(Selectors.bullet(3)).text mustBe RejectSubstituteMessages.NonTailored.b3
        document.select(Selectors.bullet(4)).text mustBe RejectSubstituteMessages.NonTailored.b4
        document.select(Selectors.bullet(5)).text mustBe RejectSubstituteMessages.NonTailored.b5
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe RejectSubstituteMessages.NonTailored.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.NonTailored.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.NonTailored.no
      }
    }
  }
}
