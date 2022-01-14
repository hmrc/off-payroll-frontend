/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.results

import assets.messages.results.{PrintPreviewMessages, UndeterminedDecisionMessages}
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.undetermined.PAYEUndeterminedView

class PAYEUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEUndeterminedView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails, workerKnown: Boolean = true): Html =
    view(form, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEUndeterminedView page" should {

    "The WhoAreYou is a Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testNoPdfResultDetails))

      workerPageChecks(Result)
      pdfPageChecks(isPdfView = false)
    }

    "The WhoAreYou is a Hirer" should {

      "when the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails))

        hirerPageChecks(Result)
        pdfPageChecks(isPdfView = false)
      }

      "when the Worker is NOT Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails, workerKnown = false))

        hirerPageChecks(Result, workerKnown = false)
        pdfPageChecks(isPdfView = false)
      }
    }
  }

  "The PAYEUndeterminedView PDF/Print page" should {

    "The WhoAreYou is a Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPdfResultDetails))

      workerPageChecks(ResultPDF)
      pdfPageChecks(isPdfView = true, isHirer = false)
    }

    "The WhoAreYou is a Hirer" should {

      "when the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails))

        hirerPageChecks(ResultPDF)
        pdfPageChecks(isPdfView = true)
      }

      "when the Worker is NOT known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails, workerKnown = false))

        hirerPageChecks(ResultPDF, workerKnown = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  "The PAYEUndeterminedView PrintPreview page" should {

    "The WhoAreYou is a Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPrintPreviewResultDetails))

      workerPageChecks(ResultPrintPreview)
      pdfPageChecks(isPdfView = true, isHirer = false)
    }

    "The WhoAreYou is a Hirer" should {

      "when the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPrintPreviewResultDetails))

        hirerPageChecks(ResultPrintPreview)
        pdfPageChecks(isPdfView = true)
      }

      "when the Worker is NOT known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPrintPreviewResultDetails, workerKnown = false))

        hirerPageChecks(ResultPrintPreview, workerKnown = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  def workerPageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(UndeterminedDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerPAYE.heading
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
          document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.WorkerPAYE.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP3
      document.select(Selectors.DoNext.p(4)).text mustBe UndeterminedDecisionMessages.Site.contactDetails
      document.select(Selectors.DoNext.p(5)).text mustBe UndeterminedDecisionMessages.downloadMsgUndetermined
    }
  }

  def hirerPageChecks(resultMode: ResultMode, workerKnown: Boolean = true)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(UndeterminedDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
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
          document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      if(workerKnown) {
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1_WorkerKnown
      } else {
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1_WorkerNotKnown
      }
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP3
      document.select(Selectors.DoNext.p(4)).text mustBe UndeterminedDecisionMessages.Site.contactDetails
      document.select(Selectors.DoNext.p(5)).text mustBe UndeterminedDecisionMessages.downloadMsgUndetermined
    }
  }
}
