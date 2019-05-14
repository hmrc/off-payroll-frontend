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

package views

import assets.messages.InteractWithStakeholdersMessages
import config.SessionKeys
import controllers.routes
import forms.InteractWithStakeholdersFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.InteractWithStakeholdersView

class InteractWithStakeholdersViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "interactWithStakeholders"

  val form = new InteractWithStakeholdersFormProvider()()

  val view = injector.instanceOf[InteractWithStakeholdersView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "InteractWithStakeholders view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.InteractWithStakeholdersController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe InteractWithStakeholdersMessages.Worker.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe InteractWithStakeholdersMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe InteractWithStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.hint(1)).text mustBe InteractWithStakeholdersMessages.Worker.hint
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe InteractWithStakeholdersMessages.Hirer.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe InteractWithStakeholdersMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe InteractWithStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.hint(1)).text mustBe InteractWithStakeholdersMessages.Hirer.hint
     }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe InteractWithStakeholdersMessages.NonTailored.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe InteractWithStakeholdersMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe InteractWithStakeholdersMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.hint(1)).text mustBe InteractWithStakeholdersMessages.NonTailored.hint
      }
    }
  }
}
