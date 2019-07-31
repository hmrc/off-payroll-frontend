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

import assets.messages.IsWorkForPrivateSectorMessages
import config.SessionKeys
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import controllers.sections.setup.routes
import forms.IsWorkForPrivateSectorFormProvider
import models.NormalMode
import models.UserType._
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.setup.IsWorkForPrivateSectorView

class IsWorkForPrivateSectorViewSpec extends YesNoViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "isWorkForPrivateSector"

  val form = new IsWorkForPrivateSectorFormProvider()()

  val view = injector.instanceOf[IsWorkForPrivateSectorView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "IsWorkForPrivateSector view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.IsWorkForPrivateSectorController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        enable(OptimisedFlow)
        document.title mustBe title(IsWorkForPrivateSectorMessages.Worker.title)
      }

      "have the correct heading" in {
        enable(OptimisedFlow)
        document.select(Selectors.heading).text mustBe IsWorkForPrivateSectorMessages.Worker.heading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)
        document.select(Selectors.multichoice(1)).text mustBe IsWorkForPrivateSectorMessages.privateOption
        document.select(Selectors.multichoice(2)).text mustBe IsWorkForPrivateSectorMessages.publicOption
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        enable(OptimisedFlow)
        document.title mustBe title(IsWorkForPrivateSectorMessages.Hirer.title)
      }

      "have the correct heading" in {
        enable(OptimisedFlow)
        document.select(Selectors.heading).text mustBe IsWorkForPrivateSectorMessages.Hirer.heading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)
        document.select(Selectors.multichoice(1)).text mustBe IsWorkForPrivateSectorMessages.privateOption
        document.select(Selectors.multichoice(2)).text mustBe IsWorkForPrivateSectorMessages.publicOption
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        enable(OptimisedFlow)
        document.title mustBe title(IsWorkForPrivateSectorMessages.NonTailored.title)
      }

      "have the correct heading" in {
        enable(OptimisedFlow)
        document.select(Selectors.heading).text mustBe IsWorkForPrivateSectorMessages.NonTailored.heading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)
        document.select(Selectors.multichoice(1)).text mustBe IsWorkForPrivateSectorMessages.privateOption
        document.select(Selectors.multichoice(2)).text mustBe IsWorkForPrivateSectorMessages.publicOption
      }
    }
  }
}
