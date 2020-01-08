/*
 * Copyright 2020 HM Revenue & Customs
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

import assets.messages.AddReferenceDetailsMessages
import forms.AddReferenceDetailsFormProvider
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.YesNoViewBehaviours
import views.html.AddReferenceDetailsView

class AddReferenceDetailsViewSpec extends YesNoViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors{
    val link = "#value > p:nth-child(3) > a"
  }

  val messageKeyPrefix = "addReferenceDetails"

  val form = new AddReferenceDetailsFormProvider()()

  val view = injector.instanceOf[AddReferenceDetailsView]

  def createView = () => view(form)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form)(req, messages, frontendAppConfig)

  "AddReferenceDetails view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

      "have the correct title" in {
        document.title mustBe title(AddReferenceDetailsMessages.title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe AddReferenceDetailsMessages.heading
      }

      "have the correct p1" in {
        document.select(Selectors.p(1)).text mustBe AddReferenceDetailsMessages.p1
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe AddReferenceDetailsMessages.yes
        document.select(Selectors.multichoice(2)).text mustBe AddReferenceDetailsMessages.no
    }
  }
}
