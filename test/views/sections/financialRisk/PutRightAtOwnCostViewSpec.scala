/*
 * Copyright 2020 HM Revenue & Customs
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

import assets.messages.{PutRightAtOwnCostsMessages, SubHeadingMessages}
import forms.sections.financialRisk.PutRightAtOwnCostFormProvider
import models.NormalMode
import models.sections.financialRisk.PutRightAtOwnCost
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.financialRisk.PutRightAtOwnCostView

class PutRightAtOwnCostViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.putRightAtOwnCost"

  val form = new PutRightAtOwnCostFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[PutRightAtOwnCostView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "PutRightAtOwnCost view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PutRightAtOwnCostsMessages.WorkerOptimised.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.yesAdditionalCost
        document.select(Selectors.multichoice(2)).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.yesAdditionalCharge
        document.select(Selectors.multichoice(3)).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.noUsualHours
        document.select(Selectors.multichoice(4)).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.noSingleEvent
        document.select(Selectors.multichoice(5)).text mustBe PutRightAtOwnCostsMessages.WorkerOptimised.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PutRightAtOwnCostsMessages.HirerOptimised.title, Some(SubHeadingMessages.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.yesAdditionalCost
        document.select(Selectors.multichoice(2)).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.yesAdditionalCharge
        document.select(Selectors.multichoice(3)).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.noUsualHours
        document.select(Selectors.multichoice(4)).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.noSingleEvent
        document.select(Selectors.multichoice(5)).text mustBe PutRightAtOwnCostsMessages.HirerOptimised.no
      }
    }
  }

  "PutRightAtOwnCost view" when {

    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- PutRightAtOwnCost.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = false)
        }
      }
    }


    for(option <- PutRightAtOwnCost.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, isChecked = true)

          for(unselectedOption <- PutRightAtOwnCost.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, isChecked = false)
          }
        }
      }
    }
  }
}

