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

  val form = new DeclarationFormProvider()()

  def createView(req: DataRequest[_], pdfDetails: PDFResultDetails): Html = view(form)(req, messages, frontendAppConfig, pdfDetails)

  "The OfficeHolderAgentView page" should {

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest, testNoPdfResultDetails))

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OfficeHolderMessages.Agent.heading
    }

    pageChecks
    pdfPageChecks(isPdfView = false)
  }

  "The OfficeHolderAgentView PDF/Print page" should {

    implicit lazy val document = asDocument(createView(agencyFakeDataRequest, testPdfResultDetails))

    "Have the correct heading" in {
      document.select(Selectors.PrintAndSave.printHeading).text mustBe OfficeHolderMessages.Agent.heading
    }

    pageChecks
    pdfPageChecks(isPdfView = true)
  }

  def pageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OfficeHolderMessages.Agent.title)
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe OfficeHolderMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OfficeHolderMessages.Agent.whyResult_p1
      document.select(Selectors.WhyResult.p(2)).text mustBe OfficeHolderMessages.Agent.whyResult_p2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe OfficeHolderMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OfficeHolderMessages.Agent.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe OfficeHolderMessages.Agent.doNext_p2
    }

    "Have a link to the Employment Status Manual" in {
      document.select("#employmentStatusManualLink").attr("href") mustBe frontendAppConfig.employmentStatusManualChapter5Url
    }
  }
}
