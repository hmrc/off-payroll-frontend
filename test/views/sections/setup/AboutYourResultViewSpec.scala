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

package views.sections.setup

import assets.messages.AboutYourResultMessages
import play.api.mvc.Call
import views.behaviours.ViewBehaviours
import views.html.sections.setup.AboutYourResultView

class AboutYourResultViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val p1 = "#content > article > form > p"
  }

  val messageKeyPrefix = "aboutYourResult"

  val view = injector.instanceOf[AboutYourResultView]

  def createView = () => view(Call("POST", "/"))(fakeRequest, messages, frontendAppConfig)

  "AboutYourResults view" must {

    behave like normalPage(
      createView,
      messageKeyPrefix,
      hasSubheading = false
    )

    behave like pageWithBackLink(createView, frontendAppConfig.govUkStartPageUrl)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AboutYourResultMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe AboutYourResultMessages.heading
    }

    "have the correct p1" in {
      document.select(Selectors.p1).text mustBe AboutYourResultMessages.p1
    }

    "have the correct warning" in {
      document.select(Selectors.exclamation).text mustBe s"${AboutYourResultMessages.p2} ${AboutYourResultMessages.p3}"
    }
  }
}
