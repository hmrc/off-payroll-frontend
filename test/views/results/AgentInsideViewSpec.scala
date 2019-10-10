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

import assets.messages.results.{InDecisionMessages, PrintPreviewMessages}
import config.SessionKeys
import forms.DeclarationFormProvider
import models.UserAnswers
import models.UserType.Agency
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import views.html.results.inside.AgentInsideView

class AgentInsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentInsideView]
  val form = new DeclarationFormProvider()()

  "The InsideAgentView page" should {

    def createView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest))

    pageChecks(isPrintPreviewView = false, isPDF = false)
    pdfPageChecks(isPdfView = false)
  }

  "The InsideAgentView PDF/PrintView page" should {

    def createPrintView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPdfResultDetails)

    implicit lazy val document = asDocument(createPrintView(agencyFakeDataRequest))

    pageChecks(isPrintPreviewView = false, isPDF = true)
    pdfPageChecks(isPdfView = true)
  }

  "The InsideAgentView Letter Print Preview page" should {

    def createPrintView(req: DataRequest[_]) = view(form)(req, messages, frontendAppConfig, testPrintPreviewResultDetails)

    implicit lazy val document = asDocument(createPrintView(agencyFakeDataRequest))

    pageChecks(isPrintPreviewView = true, isPDF = false)
    letterPrintPreviewPageChecks
  }

  def pageChecks(isPrintPreviewView: Boolean, isPDF: Boolean)(implicit document: Document) = {

    if(isPrintPreviewView) {

      "Have the correct title" in {
        document.title mustBe title(PrintPreviewMessages.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe PrintPreviewMessages.heading
      }
    } else {

      "Have the correct title" in {
        document.title mustBe title(InDecisionMessages.Agent.title)
      }

      if(isPDF) {

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.Agent.heading
        }
      } else {

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.Agent.heading
        }
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
