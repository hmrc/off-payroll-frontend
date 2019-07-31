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

import assets.messages.results.UndeterminedDecisionMessages
import config.SessionKeys
import forms.DeclarationFormProvider
import models.UserAnswers
import models.UserType.{Hirer, Worker}
import models.requests.DataRequest
import models.{PDFResultDetails, UserAnswers}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.undetermined.IR35UndeterminedView


class IR35UndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[IR35UndeterminedView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], isPrivateSector: Boolean = false, pdfDetails: PDFResultDetails): Html =
    view(form, isPrivateSector)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35UndeterminedView page" should {

    "If the UserType is Worker" should {

      "If is the Private sector" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isPrivateSector = false, testNoPdfResultDetails))

        workerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = false)
      }

      "If is the Public sector" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isPrivateSector = true, testNoPdfResultDetails))

        workerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = false)
      }
    }

    "If the UserType is Hirer" should {

      "If is the Private Sector" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isPrivateSector = true, testNoPdfResultDetails))

        hirerPageChecks(true)
        pdfPageChecks(isPdfView = false)
      }

      "If is the Public Sector" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isPrivateSector = false, testNoPdfResultDetails))

        hirerPageChecks(false)
        pdfPageChecks(isPdfView = false)
      }
    }
  }

  "The IR35UndeterminedView PDF/Print page" should {

    "If the UserType is Worker" should {

      "If is the Private sector" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isPrivateSector = false, testPdfResultDetails))

        workerPageChecks(isPrivateSector = false)
        pdfPageChecks(isPdfView = true)
      }

      "If is the Public sector" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isPrivateSector = true, testPdfResultDetails))

        workerPageChecks(isPrivateSector = true)
        pdfPageChecks(isPdfView = true)
      }
    }

    "If the UserType is Hirer" should {

      "If is the Private Sector" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isPrivateSector = true, testPdfResultDetails))

        hirerPageChecks(true)
        pdfPageChecks(isPdfView = true)
      }

      "If is the Public Sector" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isPrivateSector = false, testPdfResultDetails))

        hirerPageChecks(false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  def workerPageChecks(isPrivateSector: Boolean)(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.WorkerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerIR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.whyResult
    }

    if (isPrivateSector) {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPrivateP1
        document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPrivateP2
      }
    } else {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPublicP1
        document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextPublicP2
      }
    }
  }

  def hirerPageChecks(isPrivateSector: Boolean)(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.HirerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerIR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.whyResult
    }

    if(isPrivateSector) {
      "Have the correct Do Next section for the Private Sector" in {
        document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPrivateP1
        document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextPrivateP2
      }
    } else {
      "Have the correct Do Next section for the Public Sector" in {
        document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextPublicP1
        document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextPublicP2
      }
    }
  }
}