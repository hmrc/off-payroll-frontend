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

package views.sections.personalService

import assets.messages.{RejectSubstituteMessages, SubHeadingMessages}
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import controllers.sections.personalService.routes
import forms.RejectSubstituteFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.personalService.RejectSubstituteView

class RejectSubstituteViewSpec extends YesNoViewBehaviours {

  override def beforeEach = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.rejectSubstitute"

  val form = new RejectSubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[RejectSubstituteView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "RejectSubstitute view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.RejectSubstituteController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Optimised.Worker.title, Some(SubHeadingMessages.Optimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Optimised.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Optimised.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Optimised.Hirer.title, Some(SubHeadingMessages.Optimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Optimised.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Optimised.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RejectSubstituteMessages.Optimised.Worker.title, Some(SubHeadingMessages.Optimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RejectSubstituteMessages.Optimised.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RejectSubstituteMessages.Optimised.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RejectSubstituteMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RejectSubstituteMessages.no
      }
    }
  }
}
