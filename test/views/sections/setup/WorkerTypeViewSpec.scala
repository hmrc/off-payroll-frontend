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

import assets.messages.WorkerUsingIntermediaryMessages
import config.SessionKeys
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import forms.WorkerUsingIntermediaryFormProvider
import models.NormalMode
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.setup.WorkerUsingIntermediaryView

class WorkerTypeViewSpec extends ViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "workerUsingIntermediary"

  val form = new WorkerUsingIntermediaryFormProvider()()

  val view = injector.instanceOf[WorkerUsingIntermediaryView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WorkerType view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        enable(OptimisedFlow)
        document.title mustBe title(WorkerUsingIntermediaryMessages.Worker.title, Some(WorkerUsingIntermediaryMessages.subheading))
      }

      "have the correct heading" in {
        enable(OptimisedFlow)
        document.select(Selectors.heading).text mustBe WorkerUsingIntermediaryMessages.Worker.heading
      }

      "have the correct subheading" in {
        enable(OptimisedFlow)
        document.select(Selectors.subheading).text mustBe WorkerUsingIntermediaryMessages.subheading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)
        document.select(Selectors.multichoice(1)).text mustBe WorkerUsingIntermediaryMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WorkerUsingIntermediaryMessages.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        enable(OptimisedFlow)

        document.title mustBe title(WorkerUsingIntermediaryMessages.Hirer.title, Some(WorkerUsingIntermediaryMessages.subheading))
      }

      "have the correct heading" in {
        enable(OptimisedFlow)

        document.select(Selectors.heading).text mustBe WorkerUsingIntermediaryMessages.Hirer.heading
      }

      "have the correct subheading" in {
        enable(OptimisedFlow)

        document.select(Selectors.subheading).text mustBe WorkerUsingIntermediaryMessages.subheading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)

        document.select(Selectors.multichoice(1)).text mustBe WorkerUsingIntermediaryMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WorkerUsingIntermediaryMessages.no
      }
    }

    "If the user type is of Agency" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        enable(OptimisedFlow)
        document.title mustBe title(WorkerUsingIntermediaryMessages.NonTailored.title, Some(WorkerUsingIntermediaryMessages.subheading))
      }

      "have the correct heading" in {
        enable(OptimisedFlow)

        document.select(Selectors.heading).text mustBe WorkerUsingIntermediaryMessages.NonTailored.heading
      }

      "have the correct subheading" in {
        enable(OptimisedFlow)

        document.select(Selectors.subheading).text mustBe WorkerUsingIntermediaryMessages.subheading
      }

      "have the correct radio option messages" in {
        enable(OptimisedFlow)

        document.select(Selectors.multichoice(1)).text mustBe WorkerUsingIntermediaryMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe WorkerUsingIntermediaryMessages.no
      }
    }
  }

  "WorkerType view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        enable(OptimisedFlow)
        val doc = asDocument(createViewUsingForm(form))
        assertContainsRadioButton(doc, "value-yes", "value", "true", false)
        assertContainsRadioButton(doc, "value-no", "value", "false", false)
      }
    }

    s"rendered with a value of 'true'" must {
      s"have the 'true' radio button selected" in {
        enable(OptimisedFlow)
        val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"true"))))
        assertContainsRadioButton(doc, "value-yes", "value", "true", true)
        assertContainsRadioButton(doc, "value-no", "value", "false", false)

      }
    }
  }
}
