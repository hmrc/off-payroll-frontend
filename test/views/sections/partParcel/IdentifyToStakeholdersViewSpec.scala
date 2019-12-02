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

import assets.messages.{IdentifyToStakeholdersMessages, SubHeadingMessages}

import forms.sections.partAndParcel.IdentifyToStakeholdersFormProvider
import models.NormalMode
import models.sections.partAndParcel.IdentifyToStakeholders
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.partParcel.IdentifyToStakeholdersView

class IdentifyToStakeholdersViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.identifyToStakeholders"

  val form = new IdentifyToStakeholdersFormProvider()()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[IdentifyToStakeholdersView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "IdentifyToStakeholders view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createViewWithRequest(workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Worker.title, Some(SubHeadingMessages.Optimised.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workAsBusiness
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workAsBusiness
      }
    }

    "If the user type is of Hirer" should {

      lazy val document = asDocument(createViewWithRequest(hirerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Hirer.title, Some(SubHeadingMessages.Optimised.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.workAsBusiness
        document.select(Selectors.multichoice(4)).text mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.wouldNotHappen
      }
    }

    "If the user type is of Agency" should {

      lazy val document = asDocument(createViewWithRequest(agencyFakeRequest))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Worker.title, Some(SubHeadingMessages.Optimised.partAndParcel))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.heading
      }

      "have the correct radio options" in {
        document.select(Selectors.multichoice(1)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workForEndClient
        document.select(Selectors.multichoice(2)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workAsIndependent
        document.select(Selectors.multichoice(3)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.workAsBusiness
        document.select(Selectors.multichoice(4)).text mustBe IdentifyToStakeholdersMessages.Optimised.Worker.wouldNotHappen
      }
    }
  }

  "IdentifyToStakeholders view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- IdentifyToStakeholders.options) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }


    for(option <- IdentifyToStakeholders.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for(unselectedOption <- IdentifyToStakeholders.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
