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

import assets.messages.AccessibilityStatementMessages
import views.behaviours.ViewBehaviours
import views.html.AccessibilityStatementView

class AccessibilityStatementViewSpec extends ViewBehaviours {

  class PageSelectors(section: String = "#content") extends BaseCSSSelectors {
    val reportProblem = "#reportAccessibilityIssue"
    override val p: Int => String = i => s"$section p:nth-of-type($i)"
    override val h2: Int => String = i => s"$section h2:nth-of-type($i)"
    override val bullet: Int => String = i => s"$section ul li:nth-of-type($i)"
  }

  object Selectors extends PageSelectors
  object UsingThisServiceSelectors extends PageSelectors("#usingService")
  object HowAccessibleSelectors extends PageSelectors("#howAccessible")
  object ReportProblemsSelectors extends PageSelectors("#reportProblems")
  object NotHappySelectors extends PageSelectors("#notHappy")
  object ContactUsSelectors extends PageSelectors("#contactUs")
  object TechnicalReferenceSelectors extends PageSelectors("#technicalReference")
  object HowTestedSelectors extends PageSelectors("#howTested")

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

    "have a Using this Service section which" should {

      "have the correct h2" in {
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

    "have a How Accessible section which" should {

      "have the correct h2" in {
        document.select(HowAccessibleSelectors.h2(1)).text must include(AccessibilityStatementMessages.HowAccessible.h2)
      }

      "have the correct p1" in {
        val p1 = document.select(HowAccessibleSelectors.p(1))
        p1.text must include(AccessibilityStatementMessages.HowAccessible.p1)
        p1.select("a").attr("href") mustBe frontendAppConfig.wcagUrl
      }

      "have the correct p2" in {
        document.select(HowAccessibleSelectors.p(2)).text must include(AccessibilityStatementMessages.HowAccessible.p2)
      }
    }

    "have a Report a Problem section which" should {

      "have the correct h2" in {
        document.select(ReportProblemsSelectors.h2(1)).text must include(AccessibilityStatementMessages.ReportProblems.h2)
      }

      "have the correct p1" in {
        val p1 = document.select(ReportProblemsSelectors.p(1))
        p1.text must include(AccessibilityStatementMessages.ReportProblems.p1)
        p1.select("a").attr("href") mustBe frontendAppConfig.reportAccessibilityIssueUrl(dummyPageWithIssue)
      }
    }

    "have a Not Happy section which" should {

      "have the correct h2" in {
        document.select(NotHappySelectors.h2(1)).text must include(AccessibilityStatementMessages.NotHappy.h2)
      }

      "have the correct p1" in {
        val p1 = document.select(NotHappySelectors.p(1))
        p1.text must include(AccessibilityStatementMessages.NotHappy.p1)
        p1.select("a").first.attr("href") mustBe frontendAppConfig.eassUrl
        p1.select("a").last.attr("href") mustBe frontendAppConfig.ecniUrl
      }
    }

    "have a Contact Us section which" should {

      "have the correct h2" in {
        document.select(ContactUsSelectors.h2(1)).text must include(AccessibilityStatementMessages.ContactUs.h2)
      }

      "have the correct p1" in {
        document.select(ContactUsSelectors.p(1)).text must include(AccessibilityStatementMessages.ContactUs.p1)
      }

      "have the correct p2" in {
        document.select(ContactUsSelectors.p(2)).text must include(AccessibilityStatementMessages.ContactUs.p2)
      }

      "have the correct p3" in {
        document.select(ContactUsSelectors.p(3)).text must include(AccessibilityStatementMessages.ContactUs.p3)
      }
    }

    "have a Technical Information section which" should {

      "have the correct h2" in {
        document.select(TechnicalReferenceSelectors.h2(1)).text must include(AccessibilityStatementMessages.TechnicalInformation.h2)
      }

      "have the correct p1" in {
        document.select(TechnicalReferenceSelectors.p(1)).text must include(AccessibilityStatementMessages.TechnicalInformation.p1)
      }

      "have the correct p2" in {
        val p1 = document.select(TechnicalReferenceSelectors.p(2))
        p1.text must include(AccessibilityStatementMessages.TechnicalInformation.p2)
        p1.select("a").attr("href") mustBe frontendAppConfig.hmrcAdditionalNeedsUrl
      }
    }

    "have a How we tested this service section which" should {

      "have the correct h2" in {
        document.select(HowTestedSelectors.h2(1)).text must include(AccessibilityStatementMessages.HowTested.h2)
      }

      "have the correct p1" in {
        document.select(HowTestedSelectors.p(1)).text must include(AccessibilityStatementMessages.HowTested.p1(
          s"${frontendAppConfig.lastDacTestDay} ${frontendAppConfig.lastDacTestMonth} ${frontendAppConfig.lastDacTestYear}"
        ))
      }

      "have the correct p2" in {
        val p1 = document.select(HowTestedSelectors.p(2))
        p1.text must include(AccessibilityStatementMessages.HowTested.p2)
        p1.select("a").attr("href") mustBe frontendAppConfig.dacUrl
      }

      "have the correct p3" in {
        document.select(HowTestedSelectors.p(3)).text must include(AccessibilityStatementMessages.HowTested.p3(
          s"${frontendAppConfig.accessibilityStatementLastUpdatedDay} ${frontendAppConfig.accessibilityStatementLastUpdatedMonth} ${frontendAppConfig.accessibilityStatementLastUpdatedYear}"
        ))
      }
    }
  }
}
