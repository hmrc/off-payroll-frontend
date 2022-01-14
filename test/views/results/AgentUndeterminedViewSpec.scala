/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.results

import assets.messages.results.{PrintPreviewMessages, UndeterminedDecisionMessages}
import forms.DeclarationFormProvider
import models.requests.DataRequest
import org.jsoup.nodes.Document
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.undetermined.AgentUndeterminedView

class AgentUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentUndeterminedView]

  val form = new DeclarationFormProvider()()

  "The AgentUndeterminedView page" should {

    def createView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

    pageChecks(Result)
    pdfPageChecks(isPdfView = false)
  }

  "The AgentUndeterminedView PDF/Print page" should {

    def createView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPdfResultDetails)

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

    pageChecks(ResultPDF)
    pdfPageChecks(isPdfView = true, isHirer = false)
  }

  "The AgentUndeterminedView PrintPreview page" should {

    def createView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPrintPreviewResultDetails)

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

    pageChecks(ResultPrintPreview)
    letterPrintPreviewPageChecks
  }

  def pageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(UndeterminedDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.Agent.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe UndeterminedDecisionMessages.downloadExitMsg
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
          document.title mustBe title(UndeterminedDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.Agent.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.Agent.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p2
    }
  }
}
