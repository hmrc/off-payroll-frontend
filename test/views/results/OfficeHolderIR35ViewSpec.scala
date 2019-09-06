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
import models.AboutYouAnswer.Worker
import models.UserType.Hirer
import models.requests.DataRequest
import models.{PDFResultDetails, UserAnswers}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.inside.officeHolder.OfficeHolderIR35View

class OfficeHolderIR35ViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[OfficeHolderIR35View]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], isMakingDetermination: Boolean, pdfDetails: PDFResultDetails): Html =
    view(form, isMakingDetermination)(req, messages, frontendAppConfig, pdfDetails)

  "The OfficeHolderIR35View page" should {

    "If the UserType is Worker" should {

      "If the user is Making a Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMakingDetermination = true, testNoPdfResultDetails))

        workerPageChecks(isMakingDetermination = true)
        pdfPageChecks(isPdfView = false)
      }

      "If the user is Checking a Determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMakingDetermination = false, testNoPdfResultDetails))

        workerPageChecks(isMakingDetermination = false)
        pdfPageChecks(isPdfView = false)
      }
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isMakingDetermination = true, testNoPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = false)
    }
  }

  "The OfficeHolderIR35View PDF/Print page" should {

    "If the UserType is Worker" should {

      "If the user is Making a Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMakingDetermination = true, testPdfResultDetails))

        workerPageChecks(isMakingDetermination = true)
        pdfPageChecks(isPdfView = true)
      }

      "If the user is Checking a Determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMakingDetermination = false, testPdfResultDetails))

        workerPageChecks(isMakingDetermination = false)
        pdfPageChecks(isPdfView = true)
      }
    }

    "If the UserType is Hirer" should {

      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isMakingDetermination = false, testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(isMakingDetermination: Boolean)(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Worker.IR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Worker.IR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.whyResult_p1
    }

    if(isMakingDetermination) {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_make_p1
      }
    } else {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_check_p1
        document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_check_p2
        document.select(Selectors.DoNext.p(3)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_check_p3
        document.select(Selectors.DoNext.p(4)).text mustBe OfficeHolderMessages.Worker.IR35.doNext_check_p4
      }
    }
  }

  def hirerPageChecks(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Hirer.IR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Hirer.IR35.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Hirer.IR35.whyResult_p1
    }

    "Have the correct Do Next section which" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_p2
      document.select(Selectors.DoNext.p(3)).text mustBe OfficeHolderMessages.Hirer.IR35.doNext_p3
    }
  }
}
