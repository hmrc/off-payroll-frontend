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

import assets.messages.OfficeHolderMessages
import config.SessionKeys
import controllers.routes
import forms.OfficeHolderFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.OfficeHolderView

class OfficeHolderViewSpec extends YesNoViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "officeHolder"

  val form = new OfficeHolderFormProvider()()

  val view = injector.instanceOf[OfficeHolderView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "OfficeHolder view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.OfficeHolderController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe OfficeHolderMessages.Worker.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe OfficeHolderMessages.Worker.p1
        document.select(Selectors.p(2)).text mustBe OfficeHolderMessages.Worker.p2
        document.select(Selectors.bullet(1)).text mustBe OfficeHolderMessages.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe OfficeHolderMessages.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe OfficeHolderMessages.Worker.b3
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe OfficeHolderMessages.Worker.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OfficeHolderMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OfficeHolderMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe OfficeHolderMessages.Hirer.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe OfficeHolderMessages.Hirer.p1
        document.select(Selectors.p(2)).text mustBe OfficeHolderMessages.Hirer.p2
        document.select(Selectors.bullet(1)).text mustBe OfficeHolderMessages.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe OfficeHolderMessages.Hirer.b2
        document.select(Selectors.bullet(3)).text mustBe OfficeHolderMessages.Hirer.b3
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe OfficeHolderMessages.Hirer.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OfficeHolderMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OfficeHolderMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe OfficeHolderMessages.NonTailored.title
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe OfficeHolderMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe OfficeHolderMessages.subheading
      }

      "have the correct hints" in {
        document.select(Selectors.p(1)).text mustBe OfficeHolderMessages.NonTailored.p1
        document.select(Selectors.p(2)).text mustBe OfficeHolderMessages.NonTailored.p2
        document.select(Selectors.bullet(1)).text mustBe OfficeHolderMessages.NonTailored.b1
        document.select(Selectors.bullet(2)).text mustBe OfficeHolderMessages.NonTailored.b2
        document.select(Selectors.bullet(3)).text mustBe OfficeHolderMessages.NonTailored.b3
      }

      "have the correct exclamation (warning)" in {
        document.select(Selectors.exclamation).text mustBe OfficeHolderMessages.NonTailored.exclamation
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe OfficeHolderMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe OfficeHolderMessages.no
      }
    }
  }
}
