/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.results

import assets.messages.results.{InDecisionMessages, PrintPreviewMessages}
import forms.DeclarationFormProvider
import models.requests.DataRequest
import org.jsoup.nodes.Document
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.inside.AgentInsideView

class AgentInsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentInsideView]
  val form = new DeclarationFormProvider()()

  "The InsideAgentView page" should {

    def createView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

    pageChecks(resultMode = Result)
    pdfPageChecks(isPdfView = false)
  }

  "The InsideAgentView PDF/PrintView page" should {

    def createPrintView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPdfResultDetails)

    implicit lazy val document = asDocument(createPrintView(agencyFakeDataRequest))

    pageChecks(resultMode = ResultPDF)
    pdfPageChecks(isPdfView = true, isHirer = false)
  }

  "The InsideAgentView Letter Print Preview page" should {

    def createPrintView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPrintPreviewResultDetails)

    implicit lazy val document = asDocument(createPrintView(agencyFakeDataRequest))

    pageChecks(resultMode = ResultPrintPreview)
    letterPrintPreviewPageChecks
  }

  def pageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(InDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.Agent.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe InDecisionMessages.downloadExitMsg
        }
      case ResultPrintPreview =>
        "Have the correct title" in {
          document.title mustBe title(PrintPreviewMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe PrintPreviewMessages.heading
        }
      case ResultPDF =>
        "Have the correct title" in {
          document.title mustBe title(InDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.Agent.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.Agent.whyResultP1
      document.select(Selectors.WhyResult.p(2)).text mustBe InDecisionMessages.Agent.whyResultP2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.Agent.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe InDecisionMessages.Agent.doNextP2
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
    }
  }
}
