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
import forms.DeclarationFormProvider
import models.PDFResultDetails
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.twirl.api.Html
import views.html.results.undetermined.PAYEUndeterminedView

class PAYEUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[PAYEUndeterminedView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails, workerKnown: Boolean = true): Html =
    view(form, workerKnown)(req, messages, frontendAppConfig, pdfDetails)

  "The PAYEUndeterminedView page" should {

    "The UserType is a Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testNoPdfResultDetails))

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
      }

      workerPageChecks
      pdfPageChecks(isPdfView = false)
    }

    "The UserType is a Hirer" should {

      "when the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }

        hirerPageChecks()
        pdfPageChecks(isPdfView = false)
      }

      "when the Worker is NOT Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testNoPdfResultDetails, workerKnown = false))

        "Have the correct heading" in {
          document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }

        hirerPageChecks(workerKnown = false)
        pdfPageChecks(isPdfView = false)
      }
    }
  }

  "The PAYEUndeterminedView PDF/Print page" should {

    "The UserType is a Worker" should {

      implicit lazy val document = asDocument(createView(workerFakeDataRequest, testPdfResultDetails))

      "Have the correct heading" in {
        document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.WorkerPAYE.heading
      }

      workerPageChecks
      pdfPageChecks(isPdfView = true)
    }

    "The UserType is a Hirer" should {

      "when the Worker is Known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails))

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }

        hirerPageChecks()
        pdfPageChecks(isPdfView = true)
      }

      "when the Worker is NOT known" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest, testPdfResultDetails, workerKnown = false))

        "Have the correct heading" in {
          document.select(Selectors.PrintAndSave.printHeading).text mustBe UndeterminedDecisionMessages.HirerPAYE.heading
        }

        hirerPageChecks(workerKnown = false)
        pdfPageChecks(isPdfView = true)
      }
    }
  }

  def workerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.WorkerPAYE.title)
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.whyResult1
      document.select(Selectors.WhyResult.p(2)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.whyResult2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.WorkerPAYE.doNextP2
    }
  }

  def hirerPageChecks(workerKnown: Boolean = true)(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.HirerPAYE.title)
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.whyResult1
      document.select(Selectors.WhyResult.p(2)).text mustBe UndeterminedDecisionMessages.HirerPAYE.whyResult2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      if(workerKnown) {
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1_WorkerKnown
      } else {
        document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP1_WorkerNotKnown
      }
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe UndeterminedDecisionMessages.Site.telephone
      document.select(Selectors.DoNext.p(4)).text mustBe UndeterminedDecisionMessages.Site.email
      document.select(Selectors.DoNext.p(5)).text mustBe UndeterminedDecisionMessages.HirerPAYE.doNextP3
    }
  }
}
