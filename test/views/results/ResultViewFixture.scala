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

package views.results

import akka.http.scaladsl.model.HttpMethods
import assets.messages.results.{AdditionalPDFMessages, DecisionVersionMessages, InDecisionMessages, UserAnswersMessages}
import config.featureSwitch.OptimisedFlow
import models.AboutYouAnswer.Worker
import models.{AdditionalPdfDetails, PDFResultDetails, Section}
import models.CannotClaimAsExpense.WorkerUsedVehicle
import org.jsoup.nodes.Document
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.Html
import utils.FakeTimestamp
import viewmodels.{AnswerRow, AnswerSection}
import views.behaviours.ViewBehaviours

trait ResultViewFixture extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
    private def p(i: Int, sectionId: String) = s"$sectionId p:nth-of-type($i)"
    private def h2(sectionId: String) = s"$sectionId h2"
    private def h3(sectionId: String, i: Int) = s"$sectionId h3:nth-of-type($i)"
    object AdditionalPDF {
      val id = "#pdfDetails"
      val timestamp = s"$id h2"
      val p = (i: Int) => Selectors.p(i, id)
    }
    object WhyResult {
      val id = "#whyResult"
      val h2 = Selectors.h2(id)
      val bullet = (i: Int) => s"$id ul li:nth-of-type($i)"
      val p = (i: Int) => Selectors.p(i, id)

    }
    object DoNext {
      val id = "#doNext"
      val h2 = Selectors.h2(id)
      val p = (i: Int) => Selectors.p(i, id)
    }
    object WhatYouToldUs {
      val id = "#userAnswers"
      val h2 = Selectors.h2(id)
      val h3 = (i: Int) => Selectors.h3(id, i)
    }
    object Download {
      val id = "#download"
      val h2 = Selectors.h2(id)
      val p = (i: Int) => Selectors.p(i, id)
    }
    object DecisionVersion {
      val id = "#decisionVersion"
      val h2 = Selectors.h2(id)
      val p = (i: Int) => Selectors.p(i, id)
    }
  }

  val postAction = Call(HttpMethods.POST.value, "/")

  val version = "1.0"

  val timestamp = FakeTimestamp.timestamp()
  val fileName = "testFile"
  val testName = "Gerald"
  val testClient = "PBPlumbin"
  val testJobTitle = "Plumber"
  val testReference = "Boiler man"

  val testAdditionalPdfDetails = AdditionalPdfDetails(Some(testName), Some(testClient), Some(testJobTitle), Some(testReference), Some(fileName))
  implicit val testNoPdfResultDetails = PDFResultDetails()
  lazy val testPdfResultDetails = PDFResultDetails(printMode = true, Some(testAdditionalPdfDetails), Some(timestamp), answers)

  val answers = Seq(
    AnswerSection(Some(Messages("checkYourAnswers.setup.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q1",
          answer = "A1",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.setup
    ),
    AnswerSection(Some(Messages("checkYourAnswers.exit.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q2",
          answer = "A2",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.earlyExit
    ),
    AnswerSection(Some(Messages("checkYourAnswers.personalService.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q3",
          answer = "A3",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.personalService
    ),
    AnswerSection(Some(Messages("checkYourAnswers.control.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q4",
          answer = "A4",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.control
    ),
    AnswerSection(Some(Messages("checkYourAnswers.financialRisk.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q5",
          answer = "A5",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.financialRisk
    ),
    AnswerSection(Some(Messages("checkYourAnswers.partParcel.header")), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q6",
          answer = "A6",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.partAndParcel
    )
  )


  def pdfPageChecks(isPdfView: Boolean)(implicit document: Document): Unit = {
    if (isPdfView) {
      checkAdditionalPdfDetailsPresent
      checkUserAnswersPresent
      checkDecisionServiceVersionPresent
      checkDownloadSectionNotPresent
    } else {
      checkAdditionalPdfDetailsNotPresent
      checkUserAnswersNotPresent
      checkDecisionServiceVersionNotPresent
      checkDownloadSectionPresent
    }
  }

  def checkAdditionalPdfDetailsNotPresent(implicit document: Document) = {

    "NOT have a section which includes additional Information to supplement the PDF which" in {
      document.select(Selectors.AdditionalPDF.id).hasText mustBe false
    }
  }

  def checkAdditionalPdfDetailsPresent(implicit document: Document) = {
    "Have a section which includes additional Information to supplement the PDF which" should {

      "have the correct timestamp" in {
        document.select(Selectors.AdditionalPDF.timestamp).text mustBe AdditionalPDFMessages.timestamp(timestamp)
      }

      "have the correct completedBy" in {
        document.select(Selectors.AdditionalPDF.p(1)).text mustBe AdditionalPDFMessages.completedBy(testName)
      }

      "have the correct client" in {
        document.select(Selectors.AdditionalPDF.p(2)).text mustBe AdditionalPDFMessages.client(testClient)
      }

      "have the correct job title" in {
        document.select(Selectors.AdditionalPDF.p(3)).text mustBe AdditionalPDFMessages.jobTitle(testJobTitle)
      }

      "have the correct reference" in {
        document.select(Selectors.AdditionalPDF.p(4)).text mustBe AdditionalPDFMessages.reference(testReference)
      }
    }
  }

  def checkUserAnswersPresent(implicit document: Document) = {

    "Include a section containing the users answers" should {

      "have the correct heading" in {
        document.select(Selectors.WhatYouToldUs.h2).text mustBe UserAnswersMessages.h2
      }

      "have a section for the first set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(1)).text mustBe UserAnswersMessages.section1h3
        }
      }

      "have a section for the second set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(2)).text mustBe UserAnswersMessages.section2h3
        }
      }

      "have a section for the third set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(3)).text mustBe UserAnswersMessages.section3h3
        }
      }

      "have a section for the fourth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(4)).text mustBe UserAnswersMessages.section4h3
        }
      }

      "have a section for the fifth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(5)).text mustBe UserAnswersMessages.section5h3
        }
      }

      "have a section for the sixth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.h3(6)).text mustBe UserAnswersMessages.section6h3
        }
      }
    }
  }

  def checkUserAnswersNotPresent(implicit document: Document) = {

    "NOT include a section containing the users answers" in {
      document.select(Selectors.WhatYouToldUs.id).hasText mustBe false
    }
  }


  def checkDownloadSectionPresent(implicit document: Document) = {
    "Have the correct Download section" in {
      document.select(Selectors.Download.h2).text mustBe InDecisionMessages.downloadHeading
      document.select(Selectors.Download.p(1)).text mustBe InDecisionMessages.download_p1
    }
  }

  def checkDownloadSectionNotPresent(implicit document: Document) = {
    "Not include a Download Download section" in {
      document.select(Selectors.Download.id).hasText mustBe false
    }
  }

  def checkDecisionServiceVersionPresent(implicit document: Document) = {
    "Include a section for the Decision Service Version that" should {

      "have the correct h2" in {
        document.select(Selectors.DecisionVersion.h2).text mustBe DecisionVersionMessages.h2(frontendAppConfig.decisionVersion)
      }

      "have the correct p1" in {
        document.select(Selectors.DecisionVersion.p(1)).text mustBe DecisionVersionMessages.p1
      }
    }
  }

  def checkDecisionServiceVersionNotPresent(implicit document: Document) = {
    "NOT include a section for the Decision Service Version that" in {
        document.select(Selectors.DecisionVersion.id).hasText mustBe false
    }
  }
}
