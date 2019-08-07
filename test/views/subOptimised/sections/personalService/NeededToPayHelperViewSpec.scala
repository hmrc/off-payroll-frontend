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

package views.subOptimised.sections.personalService

import assets.messages.{NeededToPayHelperMessages, SubHeadingMessages}
import config.SessionKeys
import controllers.sections.personalService.routes
import forms.NeededToPayHelperFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.subOptimised.sections.personalService.NeededToPayHelperView

class NeededToPayHelperViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "neededToPayHelper"

  val form = new NeededToPayHelperFormProvider()()

  val view = injector.instanceOf[NeededToPayHelperView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "NeededToPayHelper view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.NeededToPayHelperController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.Worker.title, Some(SubHeadingMessages.SubOptimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.personalService
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe NeededToPayHelperMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe NeededToPayHelperMessages.Worker.p2
        document.select(Selectors.bullet(1)).text mustBe NeededToPayHelperMessages.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe NeededToPayHelperMessages.Worker.b2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.Hirer.title, Some(SubHeadingMessages.SubOptimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.personalService
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe NeededToPayHelperMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe NeededToPayHelperMessages.Hirer.p2
        document.select(Selectors.bullet(1)).text mustBe NeededToPayHelperMessages.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe NeededToPayHelperMessages.Hirer.b2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(NeededToPayHelperMessages.NonTailored.title, Some(SubHeadingMessages.SubOptimised.personalService))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe NeededToPayHelperMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe SubHeadingMessages.SubOptimised.personalService
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe NeededToPayHelperMessages.NonTailored.p1
        document.select(Selectors.p(2)).text mustBe NeededToPayHelperMessages.NonTailored.p2
        document.select(Selectors.bullet(1)).text mustBe NeededToPayHelperMessages.NonTailored.b1
        document.select(Selectors.bullet(2)).text mustBe NeededToPayHelperMessages.NonTailored.b2
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe NeededToPayHelperMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe NeededToPayHelperMessages.no
      }
    }
  }
}
