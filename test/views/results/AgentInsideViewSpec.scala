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
import models.UserType.Agency
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.results.inside.AgentInsideView

class AgentInsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentInsideView]

  "The InsideAgentView page" should {

    def createView(req: Request[_]) = view(postAction)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
    implicit lazy val document = asDocument(createView(request))

    pageChecks
    pdfPageChecks(isPdfView = false)
  }

  "The InsideAgentView PDF/PrintView page" should {

    def createPrintView(req: Request[_]) = view(postAction)(req, messages, frontendAppConfig, testPdfResultDetails)

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
    implicit lazy val document = asDocument(createPrintView(request))

    pageChecks
    pdfPageChecks(isPdfView = true)
  }

  def pageChecks(implicit document: Document) = {
    "Have the correct title" in {
      document.title mustBe title(InDecisionMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe InDecisionMessages.Agent.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe InDecisionMessages.Agent.subHeading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2).text mustBe InDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe InDecisionMessages.Agent.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe InDecisionMessages.Agent.doNext_p1
    }
  }
}
