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

package views.sections.control

import assets.messages.MoveWorkerMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.MoveWorkerFormProvider
import models.{MoveWorker, NormalMode}
import models.UserType.{Agency, Hirer, Worker}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.control.MoveWorkerView

class MoveWorkerViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.moveWorker"

  val form = new MoveWorkerFormProvider()()

  val view = injector.instanceOf[MoveWorkerView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "MoveWorker view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MoveWorkerMessages.OptimisedWorker.title, Some(MoveWorkerMessages.optimisedSubHeading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MoveWorkerMessages.OptimisedWorker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MoveWorkerMessages.OptimisedWorker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MoveWorkerMessages.OptimisedWorker.yesWithAgreement
        document.select(Selectors.multichoice(2)).text mustBe MoveWorkerMessages.OptimisedWorker.yesWithoutAgreement
        document.select(Selectors.multichoice(3)).text mustBe MoveWorkerMessages.OptimisedWorker.no
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MoveWorkerMessages.OptimisedHirer.title, Some(MoveWorkerMessages.optimisedSubHeading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MoveWorkerMessages.OptimisedHirer.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MoveWorkerMessages.OptimisedHirer.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MoveWorkerMessages.OptimisedHirer.yesWithAgreement
        document.select(Selectors.multichoice(2)).text mustBe MoveWorkerMessages.OptimisedHirer.yesWithoutAgreement
        document.select(Selectors.multichoice(3)).text mustBe MoveWorkerMessages.OptimisedHirer.no
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(MoveWorkerMessages.OptimisedWorker.title, Some(MoveWorkerMessages.optimisedSubHeading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe MoveWorkerMessages.OptimisedWorker.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe MoveWorkerMessages.OptimisedWorker.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe MoveWorkerMessages.OptimisedWorker.yesWithAgreement
        document.select(Selectors.multichoice(2)).text mustBe MoveWorkerMessages.OptimisedWorker.yesWithoutAgreement
        document.select(Selectors.multichoice(3)).text mustBe MoveWorkerMessages.OptimisedWorker.no
      }
    }
  }

  "MoveWorker view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- MoveWorker.options(true)) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- MoveWorker.options(true)) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- MoveWorker.options(true).filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
