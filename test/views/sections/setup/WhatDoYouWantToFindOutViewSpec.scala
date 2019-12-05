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

import assets.messages.WhatDoYouWantToFindOutMessages
import config.featureSwitch.FeatureSwitching
import forms.sections.setup.WhatDoYouWantToFindOutFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.mvc.Request
import views.behaviours.ViewBehaviours
import views.html.sections.setup.WhatDoYouWantToFindOutView

class WhatDoYouWantToFindOutViewSpec extends ViewBehaviours with FeatureSwitching {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "whatDoYouWantToFindOut"

  val form = new WhatDoYouWantToFindOutFormProvider()()

  val view = injector.instanceOf[WhatDoYouWantToFindOutView]

  def createView = () => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewUsingForm = (form: Form[_]) => view(form, NormalMode)(workerFakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (req: Request[_]) => view(form, NormalMode)(req, messages, frontendAppConfig)

  "WhatDoYouWantToFindOut view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = true)

    behave like pageWithBackLink(createView)

    "If the user type is of Worker" should {

      lazy val document = asDocument(createView())

      "have the correct title" in {
        document.title mustBe title(WhatDoYouWantToFindOutMessages.title, Some(WhatDoYouWantToFindOutMessages.subheading))
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe WhatDoYouWantToFindOutMessages.heading
      }

      "have the correct subheading" in {
        document.select(Selectors.subheading).text mustBe WhatDoYouWantToFindOutMessages.subheading
      }

      "have the correct radio option messages" in {
        document.select(Selectors.multichoice(1)).text mustBe WhatDoYouWantToFindOutMessages.ir35
        document.select(Selectors.multichoice(2)).text mustBe WhatDoYouWantToFindOutMessages.paye
      }
    }
  }
}
