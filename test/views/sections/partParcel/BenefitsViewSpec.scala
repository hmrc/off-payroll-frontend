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

package views.sections.partParcel

import assets.messages.BenefitsMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import controllers.sections.partParcel.routes
import forms.BenefitsFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.sections.partParcel.BenefitsView

class BenefitsViewSpec extends YesNoViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.benefits"

  val form = new BenefitsFormProvider()()

  val view = injector.instanceOf[BenefitsView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages,frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "Benefits view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.BenefitsController.onSubmit(NormalMode).url)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(BenefitsMessages.Optimised.Worker.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BenefitsMessages.Optimised.Worker.heading
      }

      "have the correct bullet points" in {
        document.select(Selectors.bullet(1)).text mustBe BenefitsMessages.Optimised.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe BenefitsMessages.Optimised.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe BenefitsMessages.Optimised.Worker.b3
        document.select(Selectors.bullet(4)).text mustBe BenefitsMessages.Optimised.Worker.b4
        document.select(Selectors.bullet(5)).text mustBe BenefitsMessages.Optimised.Worker.b5
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(BenefitsMessages.Optimised.Hirer.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BenefitsMessages.Optimised.Hirer.heading
      }

      "have the correct bullet points" in {
        document.select(Selectors.bullet(1)).text mustBe BenefitsMessages.Optimised.Hirer.b1
        document.select(Selectors.bullet(2)).text mustBe BenefitsMessages.Optimised.Hirer.b2
        document.select(Selectors.bullet(3)).text mustBe BenefitsMessages.Optimised.Hirer.b3
        document.select(Selectors.bullet(4)).text mustBe BenefitsMessages.Optimised.Hirer.b4
        document.select(Selectors.bullet(5)).text mustBe BenefitsMessages.Optimised.Hirer.b5
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(BenefitsMessages.Optimised.Worker.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BenefitsMessages.Optimised.Worker.heading
      }

      "have the correct bullet points" in {
        document.select(Selectors.bullet(1)).text mustBe BenefitsMessages.Optimised.Worker.b1
        document.select(Selectors.bullet(2)).text mustBe BenefitsMessages.Optimised.Worker.b2
        document.select(Selectors.bullet(3)).text mustBe BenefitsMessages.Optimised.Worker.b3
        document.select(Selectors.bullet(4)).text mustBe BenefitsMessages.Optimised.Worker.b4
        document.select(Selectors.bullet(5)).text mustBe BenefitsMessages.Optimised.Worker.b5
      }
    }
  }
}
