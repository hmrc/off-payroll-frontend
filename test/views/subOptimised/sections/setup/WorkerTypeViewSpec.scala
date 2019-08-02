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

package views.subOptimised.sections.setup

import assets.messages.HowProvideServicesMessages
import config.SessionKeys
import forms.WorkerTypeFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{NormalMode, WorkerType}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.setup.WorkerTypeView

class WorkerTypeViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "workerType"

  val form = new WorkerTypeFormProvider()()

  val view = injector.instanceOf[WorkerTypeView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WorkerType view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowProvideServicesMessages.Worker.title, Some(HowProvideServicesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowProvideServicesMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowProvideServicesMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowProvideServicesMessages.ltd
        document.select(Selectors.multichoice(2)).text mustBe HowProvideServicesMessages.pship
        document.select(Selectors.multichoice(3)).text mustBe HowProvideServicesMessages.thirdParty
        document.select(Selectors.multichoice(4)).text mustBe HowProvideServicesMessages.soleTrader
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowProvideServicesMessages.Hirer.title, Some(HowProvideServicesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowProvideServicesMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowProvideServicesMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowProvideServicesMessages.ltd
        document.select(Selectors.multichoice(2)).text mustBe HowProvideServicesMessages.pship
        document.select(Selectors.multichoice(3)).text mustBe HowProvideServicesMessages.thirdParty
        document.select(Selectors.multichoice(4)).text mustBe HowProvideServicesMessages.soleTrader
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowProvideServicesMessages.NonTailored.title, Some(HowProvideServicesMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowProvideServicesMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowProvideServicesMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowProvideServicesMessages.ltd
        document.select(Selectors.multichoice(2)).text mustBe HowProvideServicesMessages.pship
        document.select(Selectors.multichoice(3)).text mustBe HowProvideServicesMessages.thirdParty
        document.select(Selectors.multichoice(4)).text mustBe HowProvideServicesMessages.soleTrader
      }
    }
  }

  "WorkerType view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- WorkerType.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- WorkerType.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- WorkerType.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
