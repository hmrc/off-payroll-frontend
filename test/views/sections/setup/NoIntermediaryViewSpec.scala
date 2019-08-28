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

import assets.messages.NoIntermediaryMessages
import views.behaviours.ViewBehaviours
import views.html.sections.setup.NoIntermediaryView

class NoIntermediaryViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    override val h2 = (i: Int) => s"#content article h2:nth-of-type($i)"
    val p1 = "#content > article > p:nth-child(3)"
    val p2 = "#content > article > p:nth-child(5)"
    val startAgain = "#start-again"
    val understandingOffPayroll = "#understanding-off-payroll"
  }

  val messageKeyPrefix = "noIntermediaryResult"

  val view = injector.instanceOf[NoIntermediaryView]

  def createView = () => view(controllers.routes.StartAgainController.redirectToDisclaimer())(fakeRequest, messages, frontendAppConfig)

  "no intermediary view" must {
    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(NoIntermediaryMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe NoIntermediaryMessages.heading
    }

    "have the correct p1" in {
      document.select(Selectors.p1).text mustBe NoIntermediaryMessages.p1
    }

    "have the correct h2" in {
      document.select(Selectors.h2(1)).text mustBe NoIntermediaryMessages.subheading
    }

    "have the correct p2" in {
      document.select(Selectors.p2).text mustBe NoIntermediaryMessages.p2
      document.select(Selectors.understandingOffPayroll).attr("href") mustBe frontendAppConfig.understandingOffPayrollUrl
      document.select(Selectors.startAgain).attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
    }
  }
}
