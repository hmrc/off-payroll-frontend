/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package views.results

import assets.messages.results.{InDecisionMessages, PrintPreviewMessages}
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.inside.PAYEInsideView

class PAYEInsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEInsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails, workerKnown: Boolean = true): Html =
    view(form, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEInsideView page" should {

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

  "The PAYEInsideView PDF/Print page" should {

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

      "when the Worker is NOT Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails, workerKnown = false))

        hirerPageChecks(ResultPDF, workerKnown = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  "The PAYEInsideView PrintPreview page" should {

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

      "when the Worker is NOT Known" should {

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
          document.title mustBe title(InDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.WorkerPAYE.heading
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
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.WorkerPAYE.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.WorkerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.WorkerPAYE.doNext
      document.select(Selectors.DoNext.p(2)).text mustBe InDecisionMessages.downloadMsgWorkerDetermined
    }
  }

  def hirerPageChecks(resultMode: ResultMode, workerKnown: Boolean = true)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(InDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.HirerPAYE.heading
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
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.HirerPAYE.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.HirerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.HirerPAYE.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe InDecisionMessages.HirerPAYE.doNextP2
      if(!workerKnown) {
        document.select(Selectors.DoNext.p(3)).text mustBe InDecisionMessages.HirerPAYE.workerNotKnown
        document.select(Selectors.DoNext.p(4)).text mustBe InDecisionMessages.downloadMsgHirerDetermined
      } else {
        document.select(Selectors.DoNext.p(3)).text mustBe InDecisionMessages.downloadMsgHirerDetermined
      }
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#payeForEmployersLink").attr("href") mustBe frontendAppConfig.payeForEmployersUrl
    }
  }
}
