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
import forms.DeclarationFormProvider
import models.AboutYouAnswer.Worker
import models.{PDFResultDetails, UserAnswers}
import models.UserType.Hirer
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.{Html, HtmlFormat}
import views.html.results.outside.PAYEOutsideView

class PAYEOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEOutsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html =
    view(form, true, true, true)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEOutsideView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      implicit lazy val document = asDocument(createView(dataRequest, testNoPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = false)
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      implicit lazy val document = asDocument(createView(dataRequest, testNoPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = false)
    }
  }

  "The PAYEOutsideView PDF/Print page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      implicit lazy val document = asDocument(createView(dataRequest, testPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = true)
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      lazy val dataRequest = DataRequest(request, "id", UserAnswers("id"))

      implicit lazy val document = asDocument(createView(dataRequest, testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.WorkerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerPAYE.heading
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
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerPAYE.doNext
    }
  }

  def hirerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.HirerPAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerPAYE.heading
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
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerPAYE.doNext
    }
  }
}
