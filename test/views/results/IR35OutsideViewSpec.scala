/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package views.results

import assets.messages.results.{OutDecisionMessages, PrintPreviewMessages}
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.outside.IR35OutsideView

class IR35OutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[IR35OutsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_],
                 isMake: Boolean = false,
                 isSubstituteToDoWork: Boolean = true,
                 isClientNotControlWork: Boolean = true,
                 isIncurCostNoReclaim: Boolean = true,
                 workerKnown: Boolean = true,
                 pdfDetails: PDFResultDetails = testNoPdfResultDetails): Html =
    view(form, isMake, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35OutsideView page" should {

    "If the WhoAreYou is Worker" should {

      "If making determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = true))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerIR35.heading
        }

        workerPageChecks(Result, isMake = true)
        pdfPageChecks(isPdfView = false)
      }

      "If checking determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = false))

        workerPageChecks(Result, isMake = false)
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason3)
        }
      }

      "if no other reason is given " should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isIncurCostNoReclaim = false))

        "should display business to business reason" in {
          document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
          document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerIR35.businessReasons
        }
      }
    }

    "If the WhoAreYou is Hirer" when {

      "every reason is given" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest))

        hirerPageChecks(Result)
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason3)
        }
      }

      "if the Worker is NOT Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, workerKnown = false))

        hirerPageChecks(Result, workerKnown = false)
      }

      "if no other reason is given " should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isIncurCostNoReclaim = false))

        "should display business to business reason" in {
          document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
          document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerIR35.businessReasons
        }
      }
    }
  }

  "The IR35OutsideView PDF/Print page" should {

    "If the WhoAreYou is Worker" should {

      "If making a determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = true, pdfDetails = testPdfResultDetails))

        workerPageChecks(ResultPDF, isMake = true)
        pdfPageChecks(isPdfView = true, isHirer = false)
      }

      "If checking a determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = false, pdfDetails = testPdfResultDetails))

        workerPageChecks(ResultPDF, isMake = false)
        pdfPageChecks(isPdfView = true, isHirer = false)
      }
    }

    "If the WhoAreYou is Hirer" should {
      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isMake = false, pdfDetails = testPdfResultDetails))

      hirerPageChecks(ResultPDF)
      pdfPageChecks(isPdfView = true)
    }
  }

  "The IR35OutsideView PrintPreview page" should {

    "If the WhoAreYou is Worker" should {

      "If making a determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = true, pdfDetails = testPrintPreviewResultDetails))

        workerPageChecks(ResultPrintPreview, isMake = true)
        letterPrintPreviewPageChecks
      }

      "If checking a determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = false, pdfDetails = testPrintPreviewResultDetails))

        workerPageChecks(ResultPrintPreview, isMake = false)
        letterPrintPreviewPageChecks
      }
    }

    "If the WhoAreYou is Hirer" should {
      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isMake = false, pdfDetails = testPrintPreviewResultDetails))

      hirerPageChecks(ResultPrintPreview)
      letterPrintPreviewPageChecks
    }
  }

  def workerPageChecks(resultMode: ResultMode, isMake: Boolean)(implicit document: Document) = {

    "If the WhoAreYou is Worker" should {

      resultMode match {
        case Result =>
          "Have the correct title" in {
            document.title mustBe title(OutDecisionMessages.title)
          }
          "Have the correct heading" in {
            document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerIR35.heading
          }
          "Have the correct Download section" in {
            document.select(Selectors.Download.p(1)).text mustBe OutDecisionMessages.downloadExitMsg
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
            document.title mustBe title(OutDecisionMessages.title)
          }
          "Have the correct heading" in {
            document.select(Selectors.PrintAndSave.printHeading).text mustBe OutDecisionMessages.WorkerIR35.heading
          }
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP1
        document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB1
        document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB2
        document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB3
        document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP2
      }

      if(isMake) {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.makeDoNextP1
          document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.downloadMsgWorkerDetermined
        }
      } else {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP1
          document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP2
          document.select(Selectors.DoNext.p(3)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP3
          document.select(Selectors.DoNext.p(4)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP4
        }

        "Have a link to the Employment Status Manual" in {
          document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
        }
      }
    }
  }

  def workerSingleReasonChecks(reasonMessage: String)(implicit document: Document) : Unit ={

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP2
    }
  }

  def hirerPageChecks(resultMode: ResultMode, workerKnown: Boolean = true)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(OutDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerIR35.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe OutDecisionMessages.downloadExitMsg
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
          document.title mustBe title(OutDecisionMessages.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe OutDecisionMessages.HirerIR35.heading
        }
    }

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.HirerIR35.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultP2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerIR35.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.HirerIR35.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe OutDecisionMessages.HirerIR35.doNextP3

      if(!workerKnown) {
        document.select(Selectors.DoNext.p(4)).text mustBe OutDecisionMessages.HirerIR35.workerNotKnown
        document.select(Selectors.DoNext.p(5)).text mustBe OutDecisionMessages.downloadMsgHirerDetermined
      } else {
        document.select(Selectors.DoNext.p(4)).text mustBe OutDecisionMessages.downloadMsgHirerDetermined
      }
    }

    "Have a link to the Fee Payers responsibilities" in {
      document.select("#feePayerResponsibilitiesLink").attr("href") mustBe frontendAppConfig.feePayerResponsibilitiesUrl
    }
  }

  def hirerSingleReasonChecks(reasonMessage: String)(implicit document: Document) = {

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultP2
    }
  }

}
