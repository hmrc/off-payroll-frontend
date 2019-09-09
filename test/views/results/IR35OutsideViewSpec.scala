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
import models.UserType.Hirer
import models.requests.DataRequest
import models.{PDFResultDetails, UserAnswers}
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.outside.IR35OutsideView

class IR35OutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[IR35OutsideView]

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_],
                 isMake: Boolean = false,
                 isSubstituteToDoWork: Boolean = true,
                 isClientNotControlWork: Boolean = true,
                 isIncurCostNoReclaim: Boolean = true,
                 isBoOA: Boolean = true,
                 pdfDetails: PDFResultDetails = testNoPdfResultDetails): Html =
    view(form, isMake, isSubstituteToDoWork, isClientNotControlWork, isIncurCostNoReclaim, isBoOA)(req, messages, frontendAppConfig, pdfDetails)

  "The IR35OutsideView page" should {

    "If the UserType is Worker" should {

      "If making determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = true))

        workerPageChecks(isMake = true)
        pdfPageChecks(isPdfView = false)
      }

      "If checking determination" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = false))

        workerPageChecks(isMake = false)
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false, isBoOA = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false, isBoOA = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isBoOA = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason3)
        }

        "only booa reason is given" should {

          implicit lazy val document = asDocument(createView(workerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          workerSingleReasonChecks(reasonMessage = OutDecisionMessages.WorkerIR35.whyResultReason4)
        }
      }
    }

    "If the UserType is Hirer" when {

      "every reason is given" should {

        implicit lazy val document = asDocument(createView(hirerFakeDataRequest))

        hirerPageChecks
        pdfPageChecks(isPdfView = false)
      }

      "only display one reason" when {

        "only substituteToDoWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isClientNotControlWork = false, isIncurCostNoReclaim = false, isBoOA = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason1)
        }

        "only clientNotControlWork reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isIncurCostNoReclaim = false, isBoOA = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason2)
        }

        "only incurCostNoReclaim reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isBoOA = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason3)
        }

        "only booa reason is given" should {

          implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isSubstituteToDoWork = false, isClientNotControlWork = false, isIncurCostNoReclaim = false))
          hirerSingleReasonChecks(reasonMessage = OutDecisionMessages.HirerIR35.whyResultReason4)
        }
      }
    }
  }

  "The IR35OutsideView PDF/Print page" should {

    "If the UserType is Worker" should {

      "If Private Sector" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = true, pdfDetails = testPdfResultDetails))

        workerPageChecks(isMake = true)
        pdfPageChecks(isPdfView = true)
      }

      "If Public Sector" should {
        implicit lazy val document = asDocument(createView(workerFakeDataRequest, isMake = false, pdfDetails = testPdfResultDetails))

        workerPageChecks(isMake = false)
        pdfPageChecks(isPdfView = true)
      }
    }

    "If the UserType is Hirer" should {
      implicit lazy val document = asDocument(createView(hirerFakeDataRequest, isMake = false, pdfDetails = testPdfResultDetails))

      hirerPageChecks
      pdfPageChecks(isPdfView = true)
    }
  }

  def workerPageChecks(isMake: Boolean)(implicit document: Document) = {
    "If the UserType is Worker" should {

      "Have the correct title" in {
        document.title mustBe title(OutDecisionMessages.WorkerIR35.title)
      }

      "Have the correct heading" in {
        document.select(Selectors.heading).text mustBe OutDecisionMessages.WorkerIR35.heading
      }

      "Have the correct Why Result section" in {
        document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
        document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP1
        document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB1
        document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB2
        document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.WorkerIR35.whyResultB3
        document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP2
      }

      if(isMake) {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.makeDoNextP1
        }
      } else {
        "Have the correct Do Next section which" in {
          document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
          document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP1
          document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP2
          document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP2
          document.select(Selectors.DoNext.p(4)).text mustBe OutDecisionMessages.WorkerIR35.checkDoNextP3
        }
      }
    }
  }

  def workerSingleReasonChecks(reasonMessage: String)(implicit document: Document) : Unit ={

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.WorkerIR35.whyResultP2
    }
  }

  def hirerPageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.HirerIR35.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.HirerIR35.heading
    }

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultP1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.HirerIR35.whyResultB1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultB2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.HirerIR35.whyResultB3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultP2
    }

    "Have the correct Do Next section for the Private Sector" in {
      document.select(Selectors.DoNext.h2).text mustBe OutDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.HirerIR35.doNextP1
      document.select(Selectors.DoNext.p(2)).text mustBe OutDecisionMessages.HirerIR35.doNextP2
      document.select(Selectors.DoNext.p(3)).text mustBe OutDecisionMessages.HirerIR35.doNextP3
    }
  }

  def hirerSingleReasonChecks(reasonMessage: String)(implicit document: Document) = {

    "Have the correct Why Result section when all reasons are given" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe reasonMessage
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.HirerIR35.whyResultP2
    }
  }

}
