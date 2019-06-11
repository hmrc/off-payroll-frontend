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

import assets.messages.IdentifyToStakeholdersMessages
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.IdentifyToStakeholdersFormProvider
import models.UserType.{Agency, Hirer, Worker}
import models.{IdentifyToStakeholders, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.partParcel.IdentifyToStakeholdersView

class IdentifyToStakeholdersViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "worker.optimised.identifyToStakeholders"

  val form = new IdentifyToStakeholdersFormProvider()()

  val view = injector.instanceOf[IdentifyToStakeholdersView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "IdentifyToStakeholders view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Worker.title)
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

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Hirer.title)
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

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(IdentifyToStakeholdersMessages.Optimised.Worker.title)
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

    IdentifyToStakeholders.options.foreach(option => testOption(option.value))

    def testOption(value: String) = {
      s"option $value is selected" in {
        val (selected, unselected) = IdentifyToStakeholders.options.partition(a => a.value == value)
        val doc = asDocument(createViewUsingForm(form.bind(Map("value" -> value))))
        assertContainsRadioButton(doc, selected.head.id, "value", selected.head.value, true)
        unselected.foreach(option => assertContainsRadioButton(doc, option.id, "value", option.value, false))
      }
    }
  }
}
