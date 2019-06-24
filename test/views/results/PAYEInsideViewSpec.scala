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

import assets.messages.results.InDecisionMessages
import config.SessionKeys
import models.PDFResultDetails
import models.UserType.{Hirer, Worker}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.api.mvc.Request
import play.twirl.api.Html
import views.html.results.inside.PAYEInsideView

class PAYEInsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEInsideView]

  def createView(req: Request[_], pdfDetails: PDFResultDetails): Html =
    view(postAction)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEInsideView page" should {

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

  "The PAYEInsideView PDF/Print page" should {

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
      document.title mustBe title(InDecisionMessages.WorkerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe InDecisionMessages.WorkerPAYE.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe InDecisionMessages.WorkerPAYE.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.WorkerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.WorkerPAYE.doNext
    }
  }

  def hirerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(InDecisionMessages.HirerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe InDecisionMessages.HirerPAYE.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe InDecisionMessages.HirerPAYE.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.HirerPAYE.whyResult
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.HirerPAYE.doNext
    }
  }
}
