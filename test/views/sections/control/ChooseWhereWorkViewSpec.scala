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

import assets.messages.ChooseWhereWorkMessages
import config.SessionKeys
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import forms.ChooseWhereWorkFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{ChooseWhereWork, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.control.ChooseWhereWorkView

class ChooseWhereWorkViewSpec extends ViewBehaviours with FeatureSwitching {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.chooseWhereWork"

  val form = new ChooseWhereWorkFormProvider()()

  val view = injector.instanceOf[ChooseWhereWorkView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "ChooseWhereWork view" must {
    behave like normalPage(createView, messageKeyPrefix, false)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedWorker.title, None)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedWorker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.partly
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedHirer.title, None)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedHirer.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedHirer.partly
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ChooseWhereWorkMessages.OptimisedWorker.title, None)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ChooseWhereWorkMessages.OptimisedWorker.heading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.clientDecides
        document.select(Selectors.multichoice(2)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.workerDecides
        document.select(Selectors.multichoice(3)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.noTaskDeterminate
        document.select(Selectors.multichoice(4)).text mustBe ChooseWhereWorkMessages.OptimisedWorker.partly
      }
    }
  }

  "ChooseWhereWork view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- ChooseWhereWork.options(true)) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for(option <- ChooseWhereWork.options(true)) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- ChooseWhereWork.options(true).filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
