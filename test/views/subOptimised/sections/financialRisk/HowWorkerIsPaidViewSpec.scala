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

package views.subOptimised.sections.financialRisk

import assets.messages.HowWorkerIsPaidMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.HowWorkerIsPaidFormProvider
import models.{HowWorkerIsPaid, NormalMode}
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.financialRisk.HowWorkerIsPaidView

class HowWorkerIsPaidViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "howWorkerIsPaid"

  val form = new HowWorkerIsPaidFormProvider()()

  val view = injector.instanceOf[HowWorkerIsPaidView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "HowWorkerIsPaid view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.Worker.title, Some(HowWorkerIsPaidMessages.subheading))

      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkerIsPaidMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowWorkerIsPaidMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkerIsPaidMessages.Worker.salary
        document.select(Selectors.multichoice(2)).text mustBe HowWorkerIsPaidMessages.Worker.fixed
        document.select(Selectors.multichoice(3)).text mustBe HowWorkerIsPaidMessages.Worker.proRata
        document.select(Selectors.multichoice(4)).text mustBe HowWorkerIsPaidMessages.Worker.commision
        document.select(Selectors.multichoice(5)).text mustBe HowWorkerIsPaidMessages.Worker.profits
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.Hirer.title, Some(HowWorkerIsPaidMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkerIsPaidMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowWorkerIsPaidMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkerIsPaidMessages.Hirer.salary
        document.select(Selectors.multichoice(2)).text mustBe HowWorkerIsPaidMessages.Hirer.fixed
        document.select(Selectors.multichoice(3)).text mustBe HowWorkerIsPaidMessages.Hirer.proRata
        document.select(Selectors.multichoice(4)).text mustBe HowWorkerIsPaidMessages.Hirer.commision
        document.select(Selectors.multichoice(5)).text mustBe HowWorkerIsPaidMessages.Hirer.profits
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.NonTailored.title, Some(HowWorkerIsPaidMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkerIsPaidMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe HowWorkerIsPaidMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkerIsPaidMessages.NonTailored.salary
        document.select(Selectors.multichoice(2)).text mustBe HowWorkerIsPaidMessages.NonTailored.fixed
        document.select(Selectors.multichoice(3)).text mustBe HowWorkerIsPaidMessages.NonTailored.proRata
        document.select(Selectors.multichoice(4)).text mustBe HowWorkerIsPaidMessages.NonTailored.commision
        document.select(Selectors.multichoice(5)).text mustBe HowWorkerIsPaidMessages.NonTailored.profits
      }
    }
  }

  "HowWorkerIsPaid view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- HowWorkerIsPaid.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    disable(OptimisedFlow)
    for(option <- HowWorkerIsPaid.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- HowWorkerIsPaid.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
