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

import assets.messages.results.{OutDecisionMessages, PrintPreviewMessages}
import config.SessionKeys
import forms.DeclarationFormProvider
import models.sections.setup.AboutYouAnswer.Worker
import models.{PDFResultDetails, UserAnswers}
import models.UserType.Hirer
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.{Html, HtmlFormat}
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.outside.PAYEOutsideView

class PAYEOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEOutsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_],
                 pdfDetails: PDFResultDetails = testNoPdfResultDetails,
                 isSubstituteToDoWork: Boolean = true,
                 isClientNotControlWork: Boolean = true,
                 isIncurCostNoReclaim: Boolean = true,
                 workerKnown: Boolean = true): Html =
    view(form, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEOutsideView page" should {

    "If the UserType is Worker" should {

      "display all reasons when all reasons are given" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, testNoPdfResultDetails))

        workerPageChecks(Result)
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerPAYE.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerPAYE.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerPAYE.whyResultReason3)
        }
      }

      "when no other reasons are given" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false, isSubstituteToDoWork = false))

        "only display business to business reason" in {
          document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
          document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.businessReasons
        }
      }
    }

    "If the UserType is Hirer" should {

      "display all reasons when all reasons are given" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails))

        hirerPageChecks(Result)
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerPAYE.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerPAYE.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerPAYE.whyResultReason3)
        }
      }

      "if the Worker is NOT known" should {
        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails, workerKnown = false))

        hirerPageChecks(Result, workerKnown = false)
      }

      "when no other reasons are given" should {
        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false, isSubstituteToDoWork = false))

        "only display business to business reason" in {
          document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
          document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerPAYE.businessReasons
        }
      }
    }
  }

  "The PAYEOutsideView PDF/Print page" should {

    "If the UserType is Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPdfResultDetails))

      workerPageChecks(ResultPDF)
      pdfPageChecks(isPdfView = true)
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails))

      hirerPageChecks(ResultPDF)
      pdfPageChecks(isPdfView = true)
    }
  }

  "The PAYEOutsideView PrintPreview page" should {

    "If the UserType is Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPrintPreviewResultDetails))

      workerPageChecks(ResultPrintPreview)
      pdfPageChecks(isPdfView = true)
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPrintPreviewResultDetails))

      hirerPageChecks(ResultPrintPreview)
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(OutDecisionMessages.WorkerPAYE.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerPAYE.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe OutDecisionMessages.downloadMsg
          document.select(Selectors.Download.p(2)).text mustBe OutDecisionMessages.downloadExitMsg
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
          document.title mustBe title(OutDecisionMessages.WorkerPAYE.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe OutDecisionMessages.WorkerPAYE.heading
        }
    }

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
    }

    "Have the correct Do Next section which" in {
      document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.doNextP1
    }
  }

  def workerSingleReasonChecks(reasonMessage: String)(implicit document: Document) : Unit ={

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerPAYE.whyResultP2
    }
  }

  def hirerPageChecks(resultMode: ResultMode, workerKnown: Boolean = true)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(OutDecisionMessages.HirerPAYE.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerPAYE.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe OutDecisionMessages.downloadMsg
          document.select(Selectors.Download.p(2)).text mustBe OutDecisionMessages.downloadExitMsg
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
          document.title mustBe title(OutDecisionMessages.HirerPAYE.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe OutDecisionMessages.HirerPAYE.heading
        }
    }

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.HirerPAYE.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
    }

    "Have the correct Do Next section which" in {
      document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerPAYE.doNextP1
      if(!workerKnown) {
        document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.HirerPAYE.workerNotKnown
      }
    }
  }

  def hirerSingleReasonChecks(reasonMessage: String)(implicit document: Document) : Unit ={

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerPAYE.whyResultP2
    }
  }
}
