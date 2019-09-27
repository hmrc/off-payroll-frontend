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

import assets.messages.{HowWorkIsDoneMessages, SubHeadingMessages}
import config.SessionKeys
import forms.sections.control.HowWorkIsDoneFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import models.sections.control.HowWorkIsDone
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.control.HowWorkIsDoneView

class HowWorkIsDoneViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "howWorkIsDone"

  val form = new HowWorkIsDoneFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[HowWorkIsDoneView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "HowWorkIsDone view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.Worker.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct hint paragraph" in {
        document.select(Selectors.hint(1)).text mustBe HowWorkIsDoneMessages.Worker.hint
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.Worker.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.Worker.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.Worker.noSkilledRole
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.Worker.partly
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.Hirer.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct hint paragraph" in {
        document.select(Selectors.hint(1)).text mustBe HowWorkIsDoneMessages.Hirer.hint
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.Hirer.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.Hirer.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.Hirer.noSkilledRole
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.Hirer.partly
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkIsDoneMessages.NonTailored.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkIsDoneMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct hint paragraph" in {
        document.select(Selectors.hint(1)).text mustBe HowWorkIsDoneMessages.NonTailored.hint
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkIsDoneMessages.NonTailored.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe HowWorkIsDoneMessages.NonTailored.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe HowWorkIsDoneMessages.NonTailored.noSkilledRole
        document.select(Selectors.multichoice(4)).text mustBe HowWorkIsDoneMessages.NonTailored.partly
      }
    }
  }

  "HowWorkIsDone view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- HowWorkIsDone.options()) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- HowWorkIsDone.options()) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- HowWorkIsDone.options().filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
