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

package views.sections.setup

import assets.messages.BalanceSheetOverMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import controllers.sections.setup.routes
import forms.BalanceSheetOverFormProvider
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.setup.BalanceSheetOverView

class BalanceSheetOverViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  val messageKeyPrefix = "worker.balanceSheetOver"

  val form = new BalanceSheetOverFormProvider()()

  val view = injector.instanceOf[BalanceSheetOverView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "BalanceSheetOver view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.BalanceSheetOverController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(BalanceSheetOverMessages.Worker.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BalanceSheetOverMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe BalanceSheetOverMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe BalanceSheetOverMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(BalanceSheetOverMessages.Hirer.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BalanceSheetOverMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe BalanceSheetOverMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe BalanceSheetOverMessages.no
      }
    }
  }
}
