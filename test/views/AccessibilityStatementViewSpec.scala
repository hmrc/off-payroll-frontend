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

package views

import assets.messages.AccessibilityStatementMessages
import config.featureSwitch.OptimisedFlow
import views.behaviours.ViewBehaviours
import views.html.AccessibilityStatementView

class AccessibilityStatementViewSpec extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    val reportProblem = "#reportAccessibilityIssue"
    override val p: Int => String = i => s"#content p:nth-of-type($i)"
  }

  val messageKeyPrefix = "accessibilityStatement"

  val view = injector.instanceOf[AccessibilityStatementView]

  val dummyPageWithIssue = "/issue/page/url"

  def createView = () => view(dummyPageWithIssue)(fakeRequest, messages, frontendAppConfig)

  "FinishedChecking view" must {

    behave like normalPage(createView, messageKeyPrefix, hasSubheading = false)

    behave like pageWithBackLink(createView)

    lazy val document = asDocument(createView())

    "have the correct title" in {
      document.title mustBe title(AccessibilityStatementMessages.title)
    }

    "have the correct heading" in {
      document.select(Selectors.heading).text mustBe AccessibilityStatementMessages.heading
    }

    "have the correct p1" in {
      document.select(Selectors.p(1)).text must include(AccessibilityStatementMessages.p1)
    }

    "have the correct Report a Problem link which" should {

      lazy val reportIssueLink = document.select(Selectors.reportProblem)

      "Have the correct text" in {
        reportIssueLink.text mustBe AccessibilityStatementMessages.reportIssue
      }

      "Have the correct href" in {
        reportIssueLink.attr("href") mustBe frontendAppConfig.reportAccessibilityIssueUrl(dummyPageWithIssue)
      }
    }
  }
}
