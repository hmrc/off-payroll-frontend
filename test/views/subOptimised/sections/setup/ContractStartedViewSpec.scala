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

package views.subOptimised.sections.setup

import assets.messages.{ContractStartedMessages, SubHeadingMessages}
import config.SessionKeys
import controllers.sections.setup.routes
import forms.ContractStartedFormProvider
import models.NormalMode
import models.UserType._
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.subOptimised.sections.setup.ContractStartedView

class  ContractStartedViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "contractStarted"

  val form = new ContractStartedFormProvider()()

  val view = injector.instanceOf[ContractStartedView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ContractStarted view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.ContractStartedController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ContractStartedMessages.Worker.title, Some(SubHeadingMessages.SubOptimised.setup))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ContractStartedMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.setup
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ContractStartedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ContractStartedMessages.Hirer.title, Some(SubHeadingMessages.SubOptimised.setup))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ContractStartedMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.setup
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ContractStartedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ContractStartedMessages.NonTailored.title, Some(SubHeadingMessages.SubOptimised.setup))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ContractStartedMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.setup
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ContractStartedMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe ContractStartedMessages.no
      }
    }
  }
}
