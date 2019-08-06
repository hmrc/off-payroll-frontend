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

import assets.messages.{EquipmentExpensesMessages, SubHeadingMessages}
import config.SessionKeys
import controllers.sections.financialRisk.routes
import forms.EquipmentExpensesFormProvider
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.financialRisk.EquipmentExpensesView

class EquipmentExpensesViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "equipmentExpenses"

  val form = new EquipmentExpensesFormProvider()()

  val view = injector.instanceOf[EquipmentExpensesView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "EquipmentExpensesView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.EquipmentExpensesController.onSubmit(NormalMode).url)

    lazy val request = workerFakeRequest
    lazy val document = asDocument(createViewWithRequest(request))

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(EquipmentExpensesMessages.Worker.title, Some(SubHeadingMessages.Optimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe EquipmentExpensesMessages.Worker.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe EquipmentExpensesMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe EquipmentExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe EquipmentExpensesMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(EquipmentExpensesMessages.Hirer.title, Some(SubHeadingMessages.Optimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe EquipmentExpensesMessages.Hirer.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe EquipmentExpensesMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe EquipmentExpensesMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe EquipmentExpensesMessages.no
      }
    }
  }
}
