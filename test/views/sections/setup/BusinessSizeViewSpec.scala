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

import assets.messages.BusinessSizeMessages
import config.SessionKeys
import forms.BusinessSizeFormProvider
import models.UserType.{Hirer, Worker}
import models.{BusinessSize, NormalMode}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.setup.BusinessSizeView

class BusinessSizeViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "businessSize"

  val form = new BusinessSizeFormProvider()()

  val view = injector.instanceOf[BusinessSizeView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "BusinessSize view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(BusinessSizeMessages.Worker.title, Some(BusinessSizeMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BusinessSizeMessages.Worker.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe BusinessSizeMessages.subheading
      }

      "have the correct p1" in {
        document.select(Selectors.hint(1)).text mustBe BusinessSizeMessages.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe BusinessSizeMessages.option1
        document.select(Selectors.multichoice(2)).text mustBe BusinessSizeMessages.option2
        document.select(Selectors.multichoice(3)).text mustBe BusinessSizeMessages.option3
        document.select(Selectors.multichoice(4)).text mustBe BusinessSizeMessages.option4
      }
    }

    "If the user type is of Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val document = asDocument(createViewWithRequest(request))

      "have the correct title" in {
        document.title mustBe title(BusinessSizeMessages.Hirer.title, Some(BusinessSizeMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe BusinessSizeMessages.Hirer.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe BusinessSizeMessages.subheading
      }

      "have the correct p1" in {
        document.select(Selectors.hint(1)).text mustBe BusinessSizeMessages.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe BusinessSizeMessages.option1
        document.select(Selectors.multichoice(2)).text mustBe BusinessSizeMessages.option2
        document.select(Selectors.multichoice(3)).text mustBe BusinessSizeMessages.option3
        document.select(Selectors.multichoice(4)).text mustBe BusinessSizeMessages.option4
      }
    }
  }

  "BusinessSize view" when {
    "rendered" must {
      "contain checkboxes for the values" in {
        val doc = asDocument(createViewUsingForm(form))
        for (option <- BusinessSize.options) {
          assertContainsRadioButton(doc, option.id, "businessSize[]", option.value, false)
        }
      }
    }

    for(option <- BusinessSize.options) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' checkbox selected" in {
          val doc = asDocument(createViewUsingForm(form.bind(Map("businessSize[]" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "businessSize[]", option.value, true)

          for(unselectedOption <- BusinessSize.options.filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "businessSize[]", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
