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
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import viewmodels.{Result, ResultMode, ResultPDF, ResultPrintPreview}
import views.html.results.outside.AgentOutsideView

class AgentOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentOutsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_],
                 pdfDetails: PDFResultDetails = testNoPdfResultDetails,
                 isSubstituteToDoWork: Boolean = true,
                 isClientNotControlWork: Boolean = true,
                 isIncurCostNoReclaim: Boolean = true): Html = {
    view(form, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim)(req, messages, frontendAppConfig, pdfDetails)
  }

  "The OutAgentView page" when {

    "all reasons are given should display all reasons" should {

      implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

      pageChecks(Result)
      pdfPageChecks(isPdfView = false)
    }

    "The OutAgentView PDF/Print page" should {

      implicit lazy val document = asDocument(createView(agencyFakeDataRequest, pdfDetails = testPdfResultDetails))

      pageChecks(ResultPDF)
      pdfPageChecks(isPdfView = true)
    }

    "The OutAgentView PrintPreview page" should {

      implicit lazy val document = asDocument(createView(agencyFakeDataRequest, pdfDetails = testPrintPreviewResultDetails))

      pageChecks(ResultPrintPreview)
      letterPrintPreviewPageChecks
    }


    "only display one reason" when {

      "only substituteToDoWork reason is given" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false))
        singleReasonChecks(reasonMessage = OutDecisionMessages.Agent.whyResulReason1)
      }

      "only clientNotControlWork reason is given" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false))
        singleReasonChecks(reasonMessage = OutDecisionMessages.Agent.whyResulReason2)
      }

      "only incurCostNoReclaim reason is given" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false))
        singleReasonChecks(reasonMessage = OutDecisionMessages.Agent.whyResulReason3)
      }
      "if no other reason is given " should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isIncurCostNoReclaim = false))

        "should display business to business reason" in {
          document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
          document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.businessReasons
        }
      }
    }
  }


  def pageChecks(resultMode: ResultMode)(implicit document: Document) = {

    resultMode match {
      case Result =>
        "Have the correct title" in {
          document.title mustBe title(OutDecisionMessages.Agent.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe OutDecisionMessages.Agent.heading
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
          document.title mustBe title(OutDecisionMessages.Agent.title)
        }
        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe OutDecisionMessages.Agent.heading
        }
    }
    "Have the correct Why Result section for 4 reasons" in {

      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.Agent.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.whyResultP2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.Agent.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.Agent.doNextP2
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
    }
  }

  def singleReasonChecks(reasonMessage: String)(implicit document: Document) : Unit ={

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.whyResultP2
    }
  }
}
