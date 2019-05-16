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

package views.subOptimised.sections.control

import assets.messages.ChooseWhereWorkMessages
import config.SessionKeys
import forms.ChooseWhereWorkFormProvider
import models.{ChooseWhereWork, NormalMode}
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.control.ChooseWhereWorkView

class ChooseWhereWorkViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "chooseWhereWork"

  val form = new ChooseWhereWorkFormProvider()()

  val view = injector.instanceOf[ChooseWhereWorkView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ChooseWhereWork view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.Worker.title, Some(ChooseWhereWorkMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe ChooseWhereWorkMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.Worker.yesWorkerDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.Worker.noClientDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.Worker.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.Worker.partly
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.Hirer.title, Some(ChooseWhereWorkMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe ChooseWhereWorkMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.Hirer.yesWorkerDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.Hirer.noClientDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.Hirer.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.Hirer.partly
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.NonTailored.title, Some(ChooseWhereWorkMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe ChooseWhereWorkMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.NonTailored.yesWorkerDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.NonTailored.noClientDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.NonTailored.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.NonTailored.partly
      }
    }
  }

  "ChooseWhereWork view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- ChooseWhereWork.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- ChooseWhereWork.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- ChooseWhereWork.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
