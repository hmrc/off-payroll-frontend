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

package views.results

import akka.http.scaladsl.model.HttpMethods
import assets.messages.results._
import models.{AdditionalPdfDetails, PDFResultDetails, Section}
import org.jsoup.nodes.Document
import play.api.i18n.Messages
import play.api.mvc.Call
import utils.FakeTimestamp
import viewmodels._
import views.behaviours.ViewBehaviours

trait ResultViewFixture extends ViewBehaviours {

  override def beforeEach(): Unit = {
    super.beforeEach()

  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"

    private def p(i: Int, sectionId: String) = s"$sectionId p:nth-of-type($i)"

    private def h2(sectionId: String) = s"$sectionId h2"

    object PrintAndSave {
      val id = "#printAndSave"
      val h1 = "h1"
      val printLink = "#printLink"
      val saveAsPdf = "#saveAsPdfLink"
      val exit = "#exitLink"
      val startAgain = "#startAgainLink"
      val p = (i: Int) => Selectors.p(i, id)
      val printHeading = "span[id=result]"
    }

    object AdditionalPDF {
      val id = "#pdfDetails"
      val customisedBy = "#customisedBy"
      val client = "#clientName"
      val jobTitle = "#jobTitle"
      val reference = "#referenceDetails"
    }

    object Result {
      val id = "#result"
    }

    object AboutThisResult {
      val id = "#aboutThisResult"
      val timestamp = s"$id #timestamp"
      val decisionServiceVersionP1 = s"$id .decision-service-version p:nth-of-type(1)"
      val decisionServiceVersionP2 = s"$id .decision-service-version p:nth-of-type(2)"
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
      val setupSection = s"#${Section.setup}-heading"
      val earlyExitSection = s"#${Section.earlyExit}-heading"
      val personalServiceSection = s"#${Section.personalService}-heading"
      val controlSection = s"#${Section.control}-heading"
      val financialRiskSection = s"#${Section.financialRisk}-heading"
      val partAndParcelSection = s"#${Section.partAndParcel}-heading"
    }

    object Download {
      val id = "#download"
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
  implicit val testNoPdfResultDetails = PDFResultDetails(Result)
  lazy val testPdfResultDetails = PDFResultDetails(resultMode = ResultPDF, Some(testAdditionalPdfDetails), Some(timestamp), answers)
  lazy val testPrintPreviewResultDetails = PDFResultDetails(resultMode = ResultPrintPreview, Some(testAdditionalPdfDetails), Some(timestamp), answers)

  val answers = Seq(
    AnswerSection(Messages("checkYourAnswers.setup.header"), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q1",
          answer = "A1",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.setup
    ),
    AnswerSection(Messages("checkYourAnswers.exit.header"), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q2",
          answer = "A2",
          answerIsMessageKey = true
        ), None)
      ),
      section = Section.earlyExit
    ),
    AnswerSection(Messages("checkYourAnswers.personalService.header"), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q3",
          answer = "A3",
          answerIsMessageKey = true
        ), None)
      ),
      section = Section.personalService
    ),
    AnswerSection(Messages("checkYourAnswers.control.header"), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q4",
          answer = "A4",
          answerIsMessageKey = true
        ), None)
      ),
      section = Section.control
    ),
    AnswerSection(Messages("checkYourAnswers.financialRisk.header"), whyResult = None,
      Seq(
        (AnswerRow(
          label = "Q5",
          answer = "A5",
          answerIsMessageKey = true
        ),None)
      ),
      section = Section.financialRisk
    ),
    AnswerSection(Messages("checkYourAnswers.partParcel.header"), whyResult = None,
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


  def pdfPageChecks(isPdfView: Boolean, isHirer:Boolean = true)(implicit document: Document): Unit = {
    if (isPdfView) {
      checkAdditionalPdfDetailsPresent(isHirer)
      checkUserAnswersPresent
      checkDecisionServiceVersionPresent
    } else {
      checkAdditionalPdfDetailsNotPresent
      checkUserAnswersNotPresent
      checkDecisionServiceVersionNotPresent
    }
  }

  def letterPrintPreviewPageChecks(implicit document: Document): Unit = {
    checkPrintAndSaveSectionPresent
  }

  def checkPrintAndSaveSectionPresent(implicit document: Document): Unit = {
    "Has a section with the Print and Save as PDF options present which" should {

      "have the correct heading" in {
        document.select(Selectors.PrintAndSave.h1).text mustBe PrintAndSaveMessages.heading
      }

      "have the print link" in {
        val element = document.select(Selectors.PrintAndSave.printLink)
        element.text mustBe PrintAndSaveMessages.printLink
        element.attr("onClick") mustBe "window.print();"
        element.attr("aria-label") mustBe PrintAndSaveMessages.printLinkAria
      }

      "have the save as PDF link" in {
        val element = document.select(Selectors.PrintAndSave.saveAsPdf)
        element.text mustBe PrintAndSaveMessages.savePdfLink
        element.attr("href") mustBe controllers.routes.PDFController.downloadPDF().url
        element.attr("aria-label") mustBe PrintAndSaveMessages.savePdfLinkAria
      }

      "have the exit link" in {
        val element = document.select(Selectors.PrintAndSave.exit)
        element.text mustBe PrintAndSaveMessages.exitLink
        element.attr("href") mustBe controllers.routes.ExitSurveyController.redirectToExitSurvey().url
        element.attr("aria-label") mustBe PrintAndSaveMessages.exitLinkAria
      }

      "have the start again link" in {
        val element = document.select(Selectors.PrintAndSave.startAgain)
        element.text mustBe PrintAndSaveMessages.startAgainLink
        element.attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
        element.attr("aria-label") mustBe PrintAndSaveMessages.startAgainLinkAria
      }

      "have the correct p2" in {
        document.select(Selectors.PrintAndSave.p(2)).text mustBe PrintAndSaveMessages.p2
      }
    }
  }

  def checkAdditionalPdfDetailsNotPresent(implicit document: Document) = {

    "NOT have a section which includes additional Information to supplement the PDF which" in {
      document.select(Selectors.AdditionalPDF.id).hasText mustBe false
    }
  }

  def checkAdditionalPdfDetailsPresent(isHirer: Boolean)(implicit document: Document) = {
    "Have a section which includes additional Information to supplement the PDF which" should {

      "have the correct timestamp" in {
        document.select(Selectors.AboutThisResult.timestamp).text mustBe AdditionalPDFMessages.timestamp(timestamp)
      }

      "have the correct completedBy" in {
        document.select(Selectors.AdditionalPDF.customisedBy).text mustBe AdditionalPDFMessages.completedBy(testName)
      }

      if(isHirer){
        "have the correct organisation" in {
          document.select(Selectors.AdditionalPDF.client).text mustBe AdditionalPDFMessages.client(testClient)
        }
      } else {
        "have the correct client" in {
          document.select(Selectors.AdditionalPDF.client).text mustBe AdditionalPDFMessages.worker(testClient)
        }
      }
      "have the correct job title" in {
        document.select(Selectors.AdditionalPDF.jobTitle).text mustBe AdditionalPDFMessages.jobTitle(testJobTitle)
      }

      "have the correct reference" in {
        document.select(Selectors.AdditionalPDF.reference).text mustBe AdditionalPDFMessages.reference(testReference)
      }
    }
  }

  def checkUserAnswersPresent(implicit document: Document) = {

    "Include a section containing the users answers" should {

      "have the correct heading" in {
        document.select(Selectors.WhatYouToldUs.h2).first.text mustBe UserAnswersMessages.h2
      }

      "have a section for the first set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.setupSection).text mustBe UserAnswersMessages.section1h3
        }
      }

      "have a section for the second set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.earlyExitSection).text mustBe UserAnswersMessages.section2h3
        }
      }

      "have a section for the third set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.personalServiceSection).text mustBe UserAnswersMessages.section3h3
        }
      }

      "have a section for the fourth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.controlSection).text mustBe UserAnswersMessages.section4h3
        }
      }

      "have a section for the fifth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.financialRiskSection).text mustBe UserAnswersMessages.section5h3
        }
      }

      "have a section for the sixth set of answers that" should {

        "have the correct heading" in {
          document.select(Selectors.WhatYouToldUs.partAndParcelSection).text mustBe UserAnswersMessages.section6h3
        }
      }
    }
  }

  def checkUserAnswersNotPresent(implicit document: Document) = {

    "NOT include a section containing the users answers" in {
      document.select(Selectors.WhatYouToldUs.id).hasText mustBe false
    }
  }

  def checkDecisionServiceVersionPresent(implicit document: Document) = {
    "Include a section for the Decision Service Version that" should {

      "have the correct h2" in {
        document.select(Selectors.AboutThisResult.decisionServiceVersionP1).text mustBe DecisionVersionMessages.p1(frontendAppConfig.decisionVersion)
      }

      "have the correct p1" in {
        document.select(Selectors.AboutThisResult.decisionServiceVersionP2).text mustBe DecisionVersionMessages.p2
      }
    }
  }

  def checkDecisionServiceVersionNotPresent(implicit document: Document) = {
    "NOT include a section for the Decision Service Version that" in {
      document.select(Selectors.AboutThisResult.id).hasText mustBe false
    }
  }
}
