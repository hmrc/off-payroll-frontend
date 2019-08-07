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

import assets.messages.{PutRightAtOwnCostsMessages, SubHeadingMessages}
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.PutRightAtOwnCostFormProvider
import models.{NormalMode, PutRightAtOwnCost}
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.subOptimised.sections.financialRisk.PutRightAtOwnCostView

class PutRightAtOwnCostViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "putRightAtOwnCost"

  val form = new PutRightAtOwnCostFormProvider()()

  val view = injector.instanceOf[PutRightAtOwnCostView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "PutRightAtOwnCost view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PutRightAtOwnCostsMessages.Worker.title, Some(SubHeadingMessages.SubOptimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PutRightAtOwnCostsMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.financialRisk
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PutRightAtOwnCostsMessages.Worker.yesAdditionalCost
        document.select(Selectors.multichoice(2)).text mustBe PutRightAtOwnCostsMessages.Worker.yesAdditionalCharge
        document.select(Selectors.multichoice(3)).text mustBe PutRightAtOwnCostsMessages.Worker.noUsualHours
        document.select(Selectors.multichoice(4)).text mustBe PutRightAtOwnCostsMessages.Worker.noSingleEvent
        document.select(Selectors.multichoice(5)).text mustBe PutRightAtOwnCostsMessages.Worker.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PutRightAtOwnCostsMessages.Hirer.title, Some(SubHeadingMessages.SubOptimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PutRightAtOwnCostsMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.financialRisk
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PutRightAtOwnCostsMessages.Hirer.yesAdditionalCost
        document.select(Selectors.multichoice(2)).text mustBe PutRightAtOwnCostsMessages.Hirer.yesAdditionalCharge
        document.select(Selectors.multichoice(3)).text mustBe PutRightAtOwnCostsMessages.Hirer.noUsualHours
        document.select(Selectors.multichoice(4)).text mustBe PutRightAtOwnCostsMessages.Hirer.noSingleEvent
        document.select(Selectors.multichoice(5)).text mustBe PutRightAtOwnCostsMessages.Hirer.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(PutRightAtOwnCostsMessages.NonTailored.title, Some(SubHeadingMessages.SubOptimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe PutRightAtOwnCostsMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.financialRisk
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe PutRightAtOwnCostsMessages.NonTailored.yesAdditionalCost
        document.select(Selectors.multichoice(2)).text mustBe PutRightAtOwnCostsMessages.NonTailored.yesAdditionalCharge
        document.select(Selectors.multichoice(3)).text mustBe PutRightAtOwnCostsMessages.NonTailored.noUsualHours
        document.select(Selectors.multichoice(4)).text mustBe PutRightAtOwnCostsMessages.NonTailored.noSingleEvent
        document.select(Selectors.multichoice(5)).text mustBe PutRightAtOwnCostsMessages.NonTailored.no
      }
    }
  }

  "PutRightAtOwnCost view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- PutRightAtOwnCost.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    disable(OptimisedFlow)
    for(option <- PutRightAtOwnCost.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- PutRightAtOwnCost.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
