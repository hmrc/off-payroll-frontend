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

package views.subOptimised.sections.partParcel

import assets.messages.LineManagerDutiesMessages
import config.SessionKeys
import forms.LineManagerDutiesFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.subOptimised.sections.partParcel.LineManagerDutiesView

class LineManagerDutiesViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "lineManagerDuties"

  val form = new LineManagerDutiesFormProvider()()

  val view = injector.instanceOf[LineManagerDutiesView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "LineManagerDuties view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.Worker.title, Some(LineManagerDutiesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe LineManagerDutiesMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.bullet(1)).text mustBe LineManagerDutiesMessages.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe LineManagerDutiesMessages.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe LineManagerDutiesMessages.Worker.b3
        document.select(Selectors.bullet(4)).text mustBe LineManagerDutiesMessages.Worker.b4
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.Hirer.title, Some(LineManagerDutiesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe LineManagerDutiesMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.bullet(1)).text mustBe LineManagerDutiesMessages.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe LineManagerDutiesMessages.Hirer.b2
        document.select(Selectors.bullet(3)).text mustBe LineManagerDutiesMessages.Hirer.b3
        document.select(Selectors.bullet(4)).text mustBe LineManagerDutiesMessages.Hirer.b4
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(LineManagerDutiesMessages.NonTailored.title, Some(LineManagerDutiesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe LineManagerDutiesMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe LineManagerDutiesMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.bullet(1)).text mustBe LineManagerDutiesMessages.NonTailored.b1
        document.select(Selectors.bullet(2)).text mustBe LineManagerDutiesMessages.NonTailored.b2
        document.select(Selectors.bullet(3)).text mustBe LineManagerDutiesMessages.NonTailored.b3
        document.select(Selectors.bullet(4)).text mustBe LineManagerDutiesMessages.NonTailored.b4
      }
    }
  }
}
