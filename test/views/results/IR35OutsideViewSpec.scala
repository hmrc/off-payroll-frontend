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

import assets.messages.results.OutDecisionMessages
import config.SessionKeys
import models.AboutYouAnswer.Worker
import models.UserType.Hirer
import models.requests.DataRequest
import models.{PDFResultDetails, UserAnswers}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.outside.IR35OutsideView

class IR35OutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[IR35OutsideView]

  def createView(req: DataRequest[_], isPrivateSector: Boolean = false, pdfDetails: PDFResultDetails = testNoPdfResultDetails): Html =
    view(postAction, isPrivateSector, true, true, true)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35OutsideView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      "If Private Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        workerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = false)
      }

      "If Public Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest))

        workerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = false)
      }
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      "If Private Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = true))

        hirerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = false)
      }

      "If Public Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest))

        hirerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = false)
      }
    }
  }

  "The IR35OutsideView PDF/Print page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      "If Private Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = true, testPdfResultDetails))

        workerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = true)
      }

      "If Public Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = false, testPdfResultDetails))

        workerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = true)
      }
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      "If Private Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = true, testPdfResultDetails))

        hirerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = true)
      }

      "If Public Sector" should {
        implicit lazy val document = asDocument(createView(dataRequest, isPrivateSector = false, testPdfResultDetails))

        hirerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  def workerPageChecks(isPrivateSector: Boolean)(implicit document: Document) = {
    "If the UserType is Worker" should {

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.WorkerIR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerIR35.heading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP1
        document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB1
        document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB2
        document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB3
        document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP2
      }

      if(isPrivateSector) {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.doNextPrivate
        }
      } else {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.doNextPublic
        }
      }
    }
  }

  def hirerPageChecks(isPrivateSector: Boolean)(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.HirerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerIR35.heading
    }

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.HirerIR35.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultP2
    }

    if(isPrivateSector) {
      "Have the correct Do Next section for the Private Sector" in {
        document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerIR35.doNextPrivate
      }
    } else {
      "Have the correct Do Next section for the Public Sector" in {
        document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerIR35.doNextPublicP1
        document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.HirerIR35.doNextPublicP2
      }
    }
  }
}
