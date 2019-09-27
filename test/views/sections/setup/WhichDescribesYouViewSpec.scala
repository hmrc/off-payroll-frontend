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

import controllers.sections.setup.routes
import models.NormalMode
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.sections.setup.WhichDescribesYouView
import assets.messages.WhichDescribesYouMessages
import forms.sections.setup.WhichDescribesYouFormProvider
import models.sections.setup.WhichDescribesYouAnswer


class WhichDescribesYouViewSpec extends QuestionViewBehaviours[WhichDescribesYouAnswer] {

  object Selectors extends BaseCSSSelectors {
    val exit = "#finish-link"
  }

  val messageKeyPrefix = "whichDescribesYou"

  val form = new WhichDescribesYouFormProvider()()

  val view = injector.instanceOf[WhichDescribesYouView]

  def createView = () => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

  "WhichDescribesYou view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.AboutYouController.onSubmit(NormalMode).url)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(WhichDescribesYouMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe WhichDescribesYouMessages.heading
    }

    "have the correct first multi choice answer" in {
      document.select(Selectors.multichoice(1)).text mustBe WhichDescribesYouMessages.ir35Worker
    }

    "have the correct second multi choice answer" in {
      document.select(Selectors.multichoice(2)).text mustBe WhichDescribesYouMessages.ir35Hirer
    }

    "have the correct third multi choice answer" in {
      document.select(Selectors.multichoice(3)).text mustBe WhichDescribesYouMessages.agency
    }

    "have the correct fourth multi choice answer" in {
      document.select(Selectors.multichoice(4)).text mustBe WhichDescribesYouMessages.payeWorker
    }

    "have the correct fifth multi choice answer" in {
      document.select(Selectors.multichoice(5)).text mustBe WhichDescribesYouMessages.payeHirer
    }
  }
}
