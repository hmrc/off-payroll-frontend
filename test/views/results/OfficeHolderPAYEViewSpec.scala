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
import models.AboutYouAnswer.Worker
import models.PDFResultDetails
import models.UserType.Hirer
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.api.mvc.Request
import play.twirl.api.{Html, HtmlFormat}
import views.html.results.inside.officeHolder.OfficeHolderPAYEView

class OfficeHolderPAYEViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderPAYEView]

  def createView(req: Request[_], pdfDetails: PDFResultDetails): Html =
    view(postAction)(req, messages, frontendAppConfig, pdfDetails)

  "The OfficeHolderPAYEView page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      implicit lazy val document = asDocument(createView(request, testNoPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = false)
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      implicit lazy val document = asDocument(createView(request, testNoPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = false)
    }
  }

  "The OfficeHolderPAYEView PDF/Print page" should {

    "If the UserType is Worker" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Worker).toString)
      implicit lazy val document = asDocument(createView(request, testPdfResultDetails))

      workerPageChecks
      pdfPageChecks(isPdfView = true)
    }

    "If the UserType is Hirer" should {

      lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Hirer).toString)
      implicit lazy val document = asDocument(createView(request, testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Worker.PAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.PAYE.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Worker.PAYE.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Worker.PAYE.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Worker.PAYE.doNext_p1
    }
  }

  def hirerPageChecks(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Hirer.PAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Hirer.PAYE.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe OfficeHolderMessages.Hirer.PAYE.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Hirer.PAYE.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Hirer.PAYE.doNext_p1
    }
  }
}
