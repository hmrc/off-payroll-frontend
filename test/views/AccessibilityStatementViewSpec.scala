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

  class PageSelectors(section: String = "#content") extends BaseCSSSelectors {
    val reportProblem = "#reportAccessibilityIssue"
    override val p: Int => String = i => s"$section p:nth-of-type($i)"
    override val h2: Int => String = i => s"$section h2:nth-of-type($i)"
    override val bullet: Int => String = i => s"$section ul li:nth-of-type($i)"
  }

  object Selectors extends PageSelectors
  object UsingThisServiceSelectors extends PageSelectors("#usingService")

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

    "have the correct p2" in {
      val p2 = document.select(Selectors.p(2))
      p2.text must include(AccessibilityStatementMessages.p2)
      p2.select("a").attr("href") mustBe frontendAppConfig.govukAccessibilityStatementUrl
    }

    "have the correct p3" in {
      val p3 = document.select(Selectors.p(3))
      p3.text must include(AccessibilityStatementMessages.p3)
      p3.select("a").attr("href") mustBe frontendAppConfig.govUkStartPageUrl
    }

    "have a Using this Service section" which {

      "have the correct first h2" in {
        document.select(UsingThisServiceSelectors.h2(1)).text must include(AccessibilityStatementMessages.UsingService.h2)
      }

      "have the correct p1" in {
        document.select(UsingThisServiceSelectors.p(1)).text must include(AccessibilityStatementMessages.UsingService.p1)
      }

      "have the correct p2" in {
        document.select(UsingThisServiceSelectors.p(2)).text must include(AccessibilityStatementMessages.UsingService.p2)
      }

      "have the correct set of bullet points" in {
        document.select(UsingThisServiceSelectors.bullet(1)).text must include(AccessibilityStatementMessages.UsingService.bullet1)
        document.select(UsingThisServiceSelectors.bullet(2)).text must include(AccessibilityStatementMessages.UsingService.bullet2)
        document.select(UsingThisServiceSelectors.bullet(3)).text must include(AccessibilityStatementMessages.UsingService.bullet3)
        document.select(UsingThisServiceSelectors.bullet(4)).text must include(AccessibilityStatementMessages.UsingService.bullet4)
        document.select(UsingThisServiceSelectors.bullet(5)).text must include(AccessibilityStatementMessages.UsingService.bullet5)
      }

      "have the correct p3" in {
        document.select(UsingThisServiceSelectors.p(3)).text must include(AccessibilityStatementMessages.UsingService.p3)
      }

      "have the correct p4" in {
        val p4 = document.select(Selectors.p(4))
        p4.text must include(AccessibilityStatementMessages.UsingService.p4)
        p4.select("a").attr("href") mustBe frontendAppConfig.abilityNetUrl
      }
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
