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

import assets.messages.results.{InDecisionMessages, OutDecisionMessages, UndeterminedDecisionMessages}
import config.SessionKeys
import models.UserType.Agency
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.results.outside.AgentOutsideView

class AgentOutsideViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentOutsideView]

  lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)

  def createView(req: Request[_]) = view(postAction,true,false,false)(req, messages, frontendAppConfig)
  lazy val document = asDocument(createView(request))

  "The OutAgentView page" should {

    "Have the correct title" in {
      document.title mustBe title(OutDecisionMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe OutDecisionMessages.Agent.heading
    }

    "Have the correct Why Result section for 1 reason" in {
      document.select(Selectors.WhyResult.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2
    }

    "Have the correct Why Result section for 2 reasons" in {
      def createView2(req: Request[_]) = view(postAction,true,true,false)(req, messages, frontendAppConfig)
      lazy val document2 = asDocument(createView2(request))

      document2.select(Selectors.WhyResult.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
      document2.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document2.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document2.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.reason2
      document2.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2
    }

    "Have the correct Why Result section for 3 reasons" in {
      def createView3(req: Request[_]) = view(postAction,true,true,true)(req, messages, frontendAppConfig)
      lazy val document3 = asDocument(createView3(request))

      document3.select(Selectors.WhyResult.h2(1)).text mustBe OutDecisionMessages.whyResultHeading
      document3.select(Selectors.WhyResult.p(1)).text mustBe OutDecisionMessages.Agent.p1
      document3.select(Selectors.WhyResult.bullet(1)).text mustBe OutDecisionMessages.Agent.reason1
      document3.select(Selectors.WhyResult.bullet(2)).text mustBe OutDecisionMessages.Agent.reason2
      document3.select(Selectors.WhyResult.bullet(3)).text mustBe OutDecisionMessages.Agent.reason3
      document3.select(Selectors.WhyResult.p(2)).text mustBe OutDecisionMessages.Agent.p2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2(1)).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe OutDecisionMessages.Agent.doNext
    }

    "Have the correct Download section" in {
      document.select(Selectors.Download.h2(1)).text mustBe UndeterminedDecisionMessages.downloadHeading
      document.select(Selectors.Download.p(1)).text mustBe UndeterminedDecisionMessages.download_p1
    }
  }
}
