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

package views.sections.businessOnOwnAccount

import assets.messages.RightsOfWorkMessages
import config.featureSwitch.OptimisedFlow
import forms.sections.businessOnOwnAccount.RightsOfWorkFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.businessOnOwnAccount.RightsOfWorkView



class RightsOfWorkViewSpec extends ViewBehaviours {

  override def beforeEach = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.rightsOfWork"

  val form = new RightsOfWorkFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[RightsOfWorkView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "RightsOfWork view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Worker.title, Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Hirer.title,  Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Hirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Hirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(RightsOfWorkMessages.Worker.title, Some(RightsOfWorkMessages.Worker.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe RightsOfWorkMessages.Worker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe RightsOfWorkMessages.Worker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe RightsOfWorkMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe RightsOfWorkMessages.no
      }
    }
  }
}

