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

import assets.messages.{ScheduleOfWorkingHoursMessages, SubHeadingMessages}
import config.SessionKeys
import forms.sections.control.ScheduleOfWorkingHoursFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import models.sections.control.ScheduleOfWorkingHours
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.control.ScheduleOfWorkingHoursView

class ScheduleOfWorkingHoursViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "scheduleOfWorkingHours"

  val form = new ScheduleOfWorkingHoursFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[ScheduleOfWorkingHoursView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ScheduleOfWorkingHours view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ScheduleOfWorkingHoursMessages.Worker.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ScheduleOfWorkingHoursMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ScheduleOfWorkingHoursMessages.Worker.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe ScheduleOfWorkingHoursMessages.Worker.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe ScheduleOfWorkingHoursMessages.Worker.partly
        document.select(Selectors.multichoice(4)).text mustBe ScheduleOfWorkingHoursMessages.Worker.notApplicable
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ScheduleOfWorkingHoursMessages.Hirer.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ScheduleOfWorkingHoursMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ScheduleOfWorkingHoursMessages.Hirer.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe ScheduleOfWorkingHoursMessages.Hirer.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe ScheduleOfWorkingHoursMessages.Hirer.partly
        document.select(Selectors.multichoice(4)).text mustBe ScheduleOfWorkingHoursMessages.Hirer.notApplicable
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ScheduleOfWorkingHoursMessages.NonTailored.title, Some(SubHeadingMessages.SubOptimised.control))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ScheduleOfWorkingHoursMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.control
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ScheduleOfWorkingHoursMessages.NonTailored.yesClientDecides
        document.select(Selectors.multichoice(2)).text mustBe ScheduleOfWorkingHoursMessages.NonTailored.noWorkerDecides
        document.select(Selectors.multichoice(3)).text mustBe ScheduleOfWorkingHoursMessages.NonTailored.partly
        document.select(Selectors.multichoice(4)).text mustBe ScheduleOfWorkingHoursMessages.NonTailored.notApplicable
      }
    }
  }

  "ScheduleOfWorkingHours view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- ScheduleOfWorkingHours.options()) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- ScheduleOfWorkingHours.options()) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- ScheduleOfWorkingHours.options().filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
