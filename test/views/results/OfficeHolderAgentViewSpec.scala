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

import assets.messages.results.OfficeHolderMessages
import config.SessionKeys
import forms.DeclarationFormProvider
import models.UserType.Agency
import models.requests.DataRequest
import models.{PDFResultDetails, UserAnswers}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.inside.officeHolder.OfficeHolderAgentView

class OfficeHolderAgentViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderAgentView]

<<<<<<< HEAD
  val form = new DeclarationFormProvider()()

  def createView(req: Request[_]) = view(form)(req, messages, frontendAppConfig)
=======
  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html = view(postAction)(req, messages, frontendAppConfig, pdfDetails)
>>>>>>> 38afc4b63f0e9038575a78b2eb127d0e3924e39d

  "The OfficeHolderAgentView page" should {

    lazy val request = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString),"id",UserAnswers("id"))
    implicit lazy val document = asDocument(createView(request, testNoPdfResultDetails))

    pageChecks
    pdfPageChecks(isPdfView = false)
  }

  "The OfficeHolderAgentView PDF/Print page" should {

    lazy val request = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString),"id",UserAnswers("id"))
    implicit lazy val document = asDocument(createView(request, testPdfResultDetails))

    pageChecks
    pdfPageChecks(isPdfView = true)
  }

  def pageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Agent.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Agent.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Agent.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Agent.doNext_p1
    }
  }
}
