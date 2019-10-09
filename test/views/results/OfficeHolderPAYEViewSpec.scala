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

import assets.messages.results.{OfficeHolderMessages, PrintPreviewMessages}
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.html.results.inside.officeHolder.OfficeHolderPAYEView

class OfficeHolderPAYEViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderPAYEView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html =
    view(form)(req, messages, frontendAppConfig, pdfDetails)

  "The OfficeHolderPAYEView page" should {

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

  "The OfficeHolderPAYEView PDF/Print page" should {

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
      document.title mustBe title(OfficeHolderMessages.Worker.PAYE.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.PAYE.heading
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

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Hirer.PAYE.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Hirer.PAYE.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Hirer.PAYE.doNext_p2
    }
  }
}
