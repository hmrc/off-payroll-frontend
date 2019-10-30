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

import assets.messages.results.{OfficeHolderMessages, PrintPreviewMessages, UndeterminedDecisionMessages}
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.inside.officeHolder.OfficeHolderAgentView

class OfficeHolderAgentViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderAgentView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html = view(form)(req, messages, frontendAppConfig, pdfDetails)

  "The OfficeHolderAgentView page" should {

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest, testNoPdfResultDetails))

    pageChecks(Result)
    pdfPageChecks(isPdfView = false)
  }

  "The OfficeHolderAgentView PDF/Print page" should {

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest, testPdfResultDetails))

    pageChecks(ResultPDF)
    pdfPageChecks(isPdfView = true)
  }

  "The OfficeHolderAgentView PrintPreview page" should {

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest, testPrintPreviewResultDetails))

    pageChecks(ResultPrintPreview)
    letterPrintPreviewPageChecks
  }

  def pageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(OfficeHolderMessages.Agent.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OfficeHolderMessages.Agent.heading
        }
        "Have the correct Download section" in {
          document.select(Selectors.Download.p(1)).text mustBe OfficeHolderMessages.downloadExitMsg
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
          document.title mustBe title(OfficeHolderMessages.Agent.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe OfficeHolderMessages.Agent.heading
        }
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Agent.whyResult_p1
      document.select(Selectors.WhyResult.p(2)).text mustBe OfficeHolderMessages.Agent.whyResult_p2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Agent.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Agent.doNext_p2
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
    }
  }
}
