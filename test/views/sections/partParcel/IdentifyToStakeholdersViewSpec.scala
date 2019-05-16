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

package views.sections.partParcel

import assets.messages.IdentifyToStakeholdersMessages
import config.SessionKeys
import forms.IdentifyToStakeholdersFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{IdentifyToStakeholders, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.partParcel.IdentifyToStakeholdersView

class IdentifyToStakeholdersViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "identifyToStakeholders"

  val form = new IdentifyToStakeholdersFormProvider()()

  val view = injector.instanceOf[IdentifyToStakeholdersView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "IdentifyToStakeholders view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Worker.title, Some(IdentifyToStakeholdersMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe IdentifyToStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Worker.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Worker.workAsBusiness
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Hirer.title, Some(IdentifyToStakeholdersMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe IdentifyToStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Hirer.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Hirer.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Hirer.workAsBusiness
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.NonTailored.title, Some(IdentifyToStakeholdersMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe IdentifyToStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.NonTailored.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.NonTailored.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.NonTailored.workAsBusiness
      }
    }
  }

  "IdentifyToStakeholders view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- IdentifyToStakeholders.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- IdentifyToStakeholders.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- IdentifyToStakeholders.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
