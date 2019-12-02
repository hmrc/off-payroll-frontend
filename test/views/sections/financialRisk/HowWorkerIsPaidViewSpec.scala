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

package views.sections.financialRisk

import assets.messages.{HowWorkerIsPaidMessages, SubHeadingMessages}

import forms.sections.financialRisk.HowWorkerIsPaidFormProvider
import models.NormalMode
import models.sections.financialRisk.HowWorkerIsPaid
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.financialRisk.HowWorkerIsPaidView

class HowWorkerIsPaidViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.howWorkerIsPaid"

  val form = new HowWorkerIsPaidFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[HowWorkerIsPaidView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "HowWorkerIsPaid view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.WorkerOptimised.title, Some(SubHeadingMessages.Optimised.financialRisk))

      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.salary
        document.select(Selectors.multichoice(2)).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.fixed
        document.select(Selectors.multichoice(3)).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.proRata
        document.select(Selectors.multichoice(4)).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.commision
        document.select(Selectors.multichoice(5)).text mustBe HowWorkerIsPaidMessages.WorkerOptimised.profits
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.HirerOptimised.title, Some(SubHeadingMessages.Optimised.financialRisk))

      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe HowWorkerIsPaidMessages.HirerOptimised.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe HowWorkerIsPaidMessages.HirerOptimised.salary
        document.select(Selectors.multichoice(2)).text mustBe HowWorkerIsPaidMessages.HirerOptimised.fixed
        document.select(Selectors.multichoice(3)).text mustBe HowWorkerIsPaidMessages.HirerOptimised.proRata
        document.select(Selectors.multichoice(4)).text mustBe HowWorkerIsPaidMessages.HirerOptimised.commision
        document.select(Selectors.multichoice(5)).text mustBe HowWorkerIsPaidMessages.HirerOptimised.profits
      }
    }
  }

  "HowWorkerIsPaid optimised view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- HowWorkerIsPaid.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }


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
