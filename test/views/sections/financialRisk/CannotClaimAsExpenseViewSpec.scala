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

import assets.messages.CannotClaimAsExpenseMessages
import config.SessionKeys
import forms.CannotClaimAsExpenseFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{CannotClaimAsExpense, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.financialRisk.CannotClaimAsExpenseView

class CannotClaimAsExpenseViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "cannotClaimAsExpense"

  val form = new CannotClaimAsExpenseFormProvider()()

  val view = injector.instanceOf[CannotClaimAsExpenseView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "CannotClaimAsExpense view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(CannotClaimAsExpenseMessages.Worker.title, Some(CannotClaimAsExpenseMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe CannotClaimAsExpenseMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe CannotClaimAsExpenseMessages.subheading
      }

      "have the correct hint messages" in {
        document.select(Selectors.p(1)).text mustBe CannotClaimAsExpenseMessages.Worker.p1
        document.select(Selectors.bullet(1)).text mustBe CannotClaimAsExpenseMessages.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe CannotClaimAsExpenseMessages.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe CannotClaimAsExpenseMessages.Worker.b3
        document.select(Selectors.p(2)).text mustBe CannotClaimAsExpenseMessages.Worker.p2
        document.select(Selectors.p(3)).text mustBe CannotClaimAsExpenseMessages.Worker.p3
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe CannotClaimAsExpenseMessages.Worker.materials
        document.select(Selectors.multichoice(2)).text mustBe CannotClaimAsExpenseMessages.Worker.equipment
        document.select(Selectors.multichoice(3)).text mustBe CannotClaimAsExpenseMessages.Worker.vehicle
        document.select(Selectors.multichoice(4)).text mustBe CannotClaimAsExpenseMessages.Worker.other
        document.select(Selectors.multichoice(5)).text mustBe CannotClaimAsExpenseMessages.Worker.notRelevant
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(CannotClaimAsExpenseMessages.Hirer.title, Some(CannotClaimAsExpenseMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe CannotClaimAsExpenseMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe CannotClaimAsExpenseMessages.subheading
      }

      "have the correct hint messages" in {
        document.select(Selectors.p(1)).text mustBe CannotClaimAsExpenseMessages.Hirer.p1
        document.select(Selectors.bullet(1)).text mustBe CannotClaimAsExpenseMessages.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe CannotClaimAsExpenseMessages.Hirer.b2
        document.select(Selectors.bullet(3)).text mustBe CannotClaimAsExpenseMessages.Hirer.b3
        document.select(Selectors.p(2)).text mustBe CannotClaimAsExpenseMessages.Hirer.p2
        document.select(Selectors.p(3)).text mustBe CannotClaimAsExpenseMessages.Hirer.p3
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe CannotClaimAsExpenseMessages.Hirer.materials
        document.select(Selectors.multichoice(2)).text mustBe CannotClaimAsExpenseMessages.Hirer.equipment
        document.select(Selectors.multichoice(3)).text mustBe CannotClaimAsExpenseMessages.Hirer.vehicle
        document.select(Selectors.multichoice(4)).text mustBe CannotClaimAsExpenseMessages.Hirer.other
        document.select(Selectors.multichoice(5)).text mustBe CannotClaimAsExpenseMessages.Hirer.notRelevant
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(CannotClaimAsExpenseMessages.NonTailored.title, Some(CannotClaimAsExpenseMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe CannotClaimAsExpenseMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe CannotClaimAsExpenseMessages.subheading
      }

      "have the correct hint messages" in {
        document.select(Selectors.p(1)).text mustBe CannotClaimAsExpenseMessages.NonTailored.p1
        document.select(Selectors.bullet(1)).text mustBe CannotClaimAsExpenseMessages.NonTailored.b1
        document.select(Selectors.bullet(2)).text mustBe CannotClaimAsExpenseMessages.NonTailored.b2
        document.select(Selectors.bullet(3)).text mustBe CannotClaimAsExpenseMessages.NonTailored.b3
        document.select(Selectors.p(2)).text mustBe CannotClaimAsExpenseMessages.NonTailored.p2
        document.select(Selectors.p(3)).text mustBe CannotClaimAsExpenseMessages.NonTailored.p3
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe CannotClaimAsExpenseMessages.NonTailored.materials
        document.select(Selectors.multichoice(2)).text mustBe CannotClaimAsExpenseMessages.NonTailored.equipment
        document.select(Selectors.multichoice(3)).text mustBe CannotClaimAsExpenseMessages.NonTailored.vehicle
        document.select(Selectors.multichoice(4)).text mustBe CannotClaimAsExpenseMessages.NonTailored.other
        document.select(Selectors.multichoice(5)).text mustBe CannotClaimAsExpenseMessages.NonTailored.notRelevant
      }
    }
  }

  "CannotClaimAsExpense view" when {
    "rendered" must {
      "contain checkboxes for the values" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- CannotClaimAsExpense.options) {
          assertContainsRadioButton(doc, option.id, "cannotClaimAsExpense[]", option.value, false)
        }
      }
    }

    for(option <- CannotClaimAsExpense.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' checkbox selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("cannotClaimAsExpense[]" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "cannotClaimAsExpense[]", option.value, true)

          for(unselectedOption <- CannotClaimAsExpense.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "cannotClaimAsExpense[]", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
