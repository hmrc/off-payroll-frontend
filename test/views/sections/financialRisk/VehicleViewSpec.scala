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

import assets.messages.{SubHeadingMessages, VehicleMessages}
import config.SessionKeys
import controllers.sections.financialRisk.routes
import forms.sections.financialRisk.VehicleFormProvider
import models.NormalMode
import models.UserType.{Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.financialRisk.VehicleView

class VehicleViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.vehicle"

  val form = new VehicleFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[VehicleView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "VehicleView" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.VehicleController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(VehicleMessages.Worker.title, Some(SubHeadingMessages.Optimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe VehicleMessages.Worker.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe VehicleMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe VehicleMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe VehicleMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(VehicleMessages.Hirer.title, Some(SubHeadingMessages.Optimised.financialRisk))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe VehicleMessages.Hirer.heading
      }

      "have the correct text" in {
        document.select(Selectors.p(1)).text mustBe VehicleMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe VehicleMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe VehicleMessages.no
      }
    }
  }
}
