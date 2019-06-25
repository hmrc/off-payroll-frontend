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
import config.SessionKeys
import models.UserAnswers
import models.UserType.Agency
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import views.html.results.undetermined.AgentUndeterminedView

class AgentUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentUndeterminedView]

  lazy val request = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString),"id",UserAnswers("id"))

  "The AgentUndeterminedView page" should {

    def createView(req: DataRequest[_]) = view(postAction)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    implicit lazy val document = asDocument(createView(request))

    pageChecks
    pdfPageChecks(isPdfView = false)
  }

  "The AgentUndeterminedView PDF/Print page" should {

    def createView(req: DataRequest[_]) = view(postAction)(req, messages, frontendAppConfig, testPdfResultDetails)

    implicit lazy val document = asDocument(createView(request))

    pageChecks
    pdfPageChecks(isPdfView = true)
  }

  def pageChecks(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.Agent.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.Agent.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p2
    }
  }
}
