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

import assets.messages.results.{InDecisionMessages, OutDecisionMessages}
import config.SessionKeys
import forms.DeclarationFormProvider
import models.UserAnswers
import models.UserType.Agency
import models.requests.DataRequest
import org.jsoup.nodes.Document
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.results.outside.AgentOutsideView

class AgentOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentOutsideView]

  val form = new DeclarationFormProvider()()

  lazy val request = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString), "id", UserAnswers("id"))

  "The OutAgentView page" should {

    def createView(req: DataRequest[_]): Html =
      view(form, true, true, true)(req, messages, frontendAppConfig, testNoPdfResultDetails)

    implicit lazy val document = asDocument(createView(request))

    pageChecks
    pdfPageChecks(isPdfView = false)
  }

  "The OutAgentView PDF/Print page" should {

    def createView(req: DataRequest[_]): Html = view(form, true, true, true)(req, messages, frontendAppConfig, testPdfResultDetails)

    implicit lazy val document = asDocument(createView(request))

    pageChecks
    pdfPageChecks(isPdfView = true)
  }

  def pageChecks(implicit document: Document) = {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.Agent.heading
    }

    "Have the correct Why Result section for 2 reasons" in {
      def createView2(req: DataRequest[_]) = view(form,true,true,false)(req, messages, frontendAppConfig,testPdfResultDetails)

      lazy val document2 = asDocument(createView2(request))

      document2.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document2.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document2.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document2.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.reason2
      document2.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2
    }

    "Have the correct Why Result section for 3 reasons" in {

      def createView3(req: DataRequest[_]) = view(form,true,true,true)(req, messages, frontendAppConfig,testPdfResultDetails)

      lazy val document3 = asDocument(createView3(request))

      document3.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document3.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document3.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document3.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.reason2
      document3.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.Agent.reason3
      document3.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2

    }
    "Have the correct Why Result section for 3 reasons for no details" in {
      document.select(Selectors.WhyResult.h2).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.reason2
      document.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.Agent.reason3
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2
    }
    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.Agent.doNext
    }
  }
}
