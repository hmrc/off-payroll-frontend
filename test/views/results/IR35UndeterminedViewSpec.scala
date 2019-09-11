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

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html = view(form)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35UndeterminedView page" should {

    "If the UserType is Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testNoPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = false)
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = false)
    }
  }

  "The IR35UndeterminedView PDF/Print page" should {

    "If the UserType is Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = true)
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.WorkerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerIR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.whyResult1
      document.select(Selectors.WhyResult.p(2)).text mustBe UndeterminedDecisionMessages.WorkerIR35.whyResult2
    }

    "Have the correct Do Next section which" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerIR35.doNextP1
      document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextP2
      document.select(Selectors.DoNext.p(3)).text() mustBe UndeterminedDecisionMessages.Site.telephone
      document.select(Selectors.DoNext.p(4)).text() mustBe UndeterminedDecisionMessages.Site.email
      document.select(Selectors.DoNext.p(5)).text() mustBe UndeterminedDecisionMessages.WorkerIR35.doNextP3
    }
  }

  def hirerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.HirerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerIR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.whyResult1
      document.select(Selectors.WhyResult.p(2)).text mustBe UndeterminedDecisionMessages.HirerIR35.whyResult2
    }

    "Have the correct Do Next section for the Public Sector" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerIR35.doNextP1
      document.select(Selectors.DoNext.p(2)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextP2
      document.select(Selectors.DoNext.p(3)).text() mustBe UndeterminedDecisionMessages.Site.telephone
      document.select(Selectors.DoNext.p(4)).text() mustBe UndeterminedDecisionMessages.Site.email
      document.select(Selectors.DoNext.p(5)).text() mustBe UndeterminedDecisionMessages.HirerIR35.doNextP3
    }
  }
}