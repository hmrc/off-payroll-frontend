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

import assets.messages.HowWorkerIsPaidMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.HowWorkerIsPaidFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{HowWorkerIsPaid, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.financialRisk.HowWorkerIsPaidView

class HowWorkerIsPaidViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.howWorkerIsPaid"

  val form = new HowWorkerIsPaidFormProvider()()

  val view = injector.instanceOf[HowWorkerIsPaidView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "HowWorkerIsPaid view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.WorkerOptimised.title)

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

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(HowWorkerIsPaidMessages.HirerOptimised.title)

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

    HowWorkerIsPaid.options.foreach(option => testOption(option.value))

    def testOption(value: String) = {
      s"option $value is selected" in {
        val (selected, unselected) = HowWorkerIsPaid.options.partition(a => a.value == value)
        val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> value))))
        assertContainsRadioButton(doc, selected.head.id, "value", selected.head.value, true)
        unselected.foreach(option => assertContainsRadioButton(doc, option.id, "value", option.value, false))
      }
    }
  }
}
