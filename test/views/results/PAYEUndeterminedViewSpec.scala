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
import models.PDFResultDetails
import models.UserType.{Hirer, Worker}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.api.mvc.Request
import play.twirl.api.Html
import views.html.results.undetermined.PAYEUndeterminedView

class PAYEUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEUndeterminedView]

  def createView(req: Request[_], pdfDetails: PDFResultDetails): Html =
    view(postAction)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEUndeterminedView page" should {

    "The UserType is a Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      implicit lazy val document = asDocument(createView(request, testNoPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = false)
    }

    "The UserType is a Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      implicit lazy val document = asDocument(createView(request, testNoPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = false)
    }
  }

  "The PAYEUndeterminedView PDF/Print page" should {

    "The UserType is a Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      implicit lazy val document = asDocument(createView(request, testPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = true)
    }

    "The UserType is a Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      implicit lazy val document = asDocument(createView(request, testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.WorkerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.WorkerPAYE.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP2
    }
  }

  def hirerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.HirerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP2
    }
  }
}
