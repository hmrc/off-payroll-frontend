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
import forms.DeclarationFormProvider
import models.sections.setup.AboutYouAnswer.Worker
import models.{PDFResultDetails, UserAnswers}
import models.UserType.Hirer
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.{Html, HtmlFormat}
import views.html.results.inside.IR35InsideView

class IR35InsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[IR35InsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_],
                 workerKnown: Boolean = true,
                 isMakingNewDetermination: Boolean = false,
                 pdfDetails: PDFResultDetails = testNoPdfResultDetails): Html =
    view(form, isMakingNewDetermination, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35InsideView page" should {

    "If the UserType is Worker" should {

      "is Checking a Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.WorkerIR35.heading
        }

        workerPageChecks(isMakingNewDetermination = false)
        pdfPageChecks(isPdfView = false)
      }

      "is Making a New Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMakingNewDetermination = true))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.WorkerIR35.heading
        }

        workerPageChecks(isMakingNewDetermination = true)
        pdfPageChecks(isPdfView = false)
      }
    }

    "If the UserType is Hirer" should {

      "if the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, workerKnown = true))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.HirerIR35.heading
        }

        hirerPageChecks(workerKnown = true)
        pdfPageChecks(isPdfView = false)
      }

      "if the Worker is NOT Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, workerKnown = false))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe InDecisionMessages.HirerIR35.heading
        }

        hirerPageChecks(workerKnown = false)
        pdfPageChecks(isPdfView = false)
      }
    }
  }

  "The IR35InsideView PDF/Print page" should {

    "If the UserType is Worker" should {

      "is Checking a Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, workerKnown = true, isMakingNewDetermination = false, testPdfResultDetails))

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.WorkerIR35.heading
        }

        workerPageChecks(isMakingNewDetermination = false)
        pdfPageChecks(isPdfView = true)
      }

      "is Making a New Determination" should {

        implicit lazy val document = asDocument(createView(workerFakeDataRequest, workerKnown = true, isMakingNewDetermination = true, testPdfResultDetails))

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.WorkerIR35.heading
        }

        workerPageChecks(isMakingNewDetermination = true)
        pdfPageChecks(isPdfView = true)
      }
    }

    "If the UserType is Hirer" should {

      "if the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, workerKnown = true, isMakingNewDetermination = true, testPdfResultDetails))

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.HirerIR35.heading
        }

        hirerPageChecks(workerKnown = true)
        pdfPageChecks(isPdfView = true)
      }

      "if the Worker is Not Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, workerKnown = false, isMakingNewDetermination = true, testPdfResultDetails))


        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe InDecisionMessages.HirerIR35.heading
        }

        hirerPageChecks(workerKnown = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }


  def hirerPageChecks(workerKnown: Boolean)(implicit document: Document): Unit = {

    "Have the correct title" in {
      document.title mustBe title(InDecisionMessages.HirerIR35.title)
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.HirerIR35.whyResultP1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.HirerIR35.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe InDecisionMessages.HirerIR35.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe InDecisionMessages.HirerIR35.doNextP3
      if(!workerKnown) {
        document.select(Selectors.DoNext.p(4)).text mustBe InDecisionMessages.HirerIR35.workerNotKnown
      }
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#feePayerResponsibilitiesLink").attr("href") mustBe frontendAppConfig.feePayerResponsibilitiesUrl
    }
  }


  def workerPageChecks(isMakingNewDetermination: Boolean)(implicit document: Document): Unit = {

    "Have the correct title" in {
      document.title mustBe title(InDecisionMessages.WorkerIR35.title)
    }
    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.WorkerIR35.whyResultP1
    }

    if(isMakingNewDetermination) {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.WorkerIR35.makeDoNextP1
      }
    } else {
      "Have the correct Do Next section which" in {
        document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
        document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.WorkerIR35.checkDoNextP1
        document.select(Selectors.DoNext.p(2)).text mustBe InDecisionMessages.WorkerIR35.checkDoNextP2
        document.select(Selectors.DoNext.p(3)).text mustBe InDecisionMessages.WorkerIR35.checkDoNextP3
        document.select(Selectors.DoNext.p(4)).text mustBe InDecisionMessages.WorkerIR35.checkDoNextP4
        document.select(Selectors.DoNext.p(5)).text mustBe InDecisionMessages.WorkerIR35.checkDoNextP5
      }

      "Have a link to the Employment Status Manual" in {
        document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
      }
    }
  }
}
