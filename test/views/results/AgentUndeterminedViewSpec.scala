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
import forms.DeclarationFormProvider
import models.UserType.Agency
import play.api.libs.json.Json
import play.api.mvc.Request
import views.html.results.undetermined.AgentUndeterminedView

class AgentUndeterminedViewSpec extends ResultViewFixture {

  val view = injector.instanceOf[AgentUndeterminedView]

  val form = new DeclarationFormProvider()()

  def createView(req: Request[_]) = view(form)(req, messages, frontendAppConfig)

  "The InsideAgentView page" should {

    lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)
    lazy val document = asDocument(createView(request))

    "Have the correct title" in {
      document.title mustBe title(UndeterminedDecisionMessages.Agent.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe UndeterminedDecisionMessages.Agent.heading
    }

    "Have the correct Why Result section" in {
      document.select(Selectors.WhyResult.h2(1)).text mustBe UndeterminedDecisionMessages.whyResultHeading
      document.select(Selectors.WhyResult.p(1)).text mustBe UndeterminedDecisionMessages.Agent.whyResult_p1
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.DoNext.h2(1)).text mustBe UndeterminedDecisionMessages.doNextHeading
      document.select(Selectors.DoNext.p(1)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p1
      document.select(Selectors.DoNext.p(2)).text mustBe UndeterminedDecisionMessages.Agent.doNext_p2
    }

    "Have the correct Download section" in {
      document.select(Selectors.Download.h2(1)).text mustBe UndeterminedDecisionMessages.downloadHeading
      document.select(Selectors.Download.p(1)).text mustBe UndeterminedDecisionMessages.download_p1
    }
  }
}
