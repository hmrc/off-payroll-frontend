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

import akka.http.scaladsl.model.HttpMethods
import assets.messages.results.{AgentResultMessages, InDecisionMessages, UndeterminedDecisionMessages}
import config.SessionKeys
import config.featureSwitch.OptimisedFlow
import forms.DeclarationFormProvider
import models.UserType.Agency
import play.api.libs.json.Json
import play.api.mvc.{Call, Request}
import views.ViewSpecBase
import views.html.results.AgentOutView

class AgentOutViewSpec extends ViewSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  object Selectors extends BaseCSSSelectors {
    override val subheading = "p.font-large"
  }

  val form = new DeclarationFormProvider()()

  val view = injector.instanceOf[AgentOutView]

  val postAction = Call(HttpMethods.POST.value, "/")

  lazy val request = fakeRequest.withSession(SessionKeys.userType -> Json.toJson(Agency).toString)

  def createView(req: Request[_]) = view(form, postAction,true,false,false)(req, messages, frontendAppConfig)
  lazy val document = asDocument(createView(request))

  "The InsideAgentView page" should {

    "Have the correct title" in {
      document.title mustBe title(AgentResultMessages.Out.title)
    }

    "Have the correct heading" in {
      document.select(Selectors.heading).text mustBe AgentResultMessages.Out.heading
    }

    "Have the correct subheading" in {
      document.select(Selectors.subheading).text mustBe AgentResultMessages.Out.subHeading
    }

    "Have the correct Why Result section for 1 reason" in {
      document.select(Selectors.h2(1)).text mustBe AgentResultMessages.whyResultHeading
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p1
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason1
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p2
    }

    "Have the correct Why Result section for 2 reasons" in {
      document.select(Selectors.h2(1)).text mustBe AgentResultMessages.whyResultHeading
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p1
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason1
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason2
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p2
    }

    "Have the correct Why Result section for 3 reasons" in {
      document.select(Selectors.h2(1)).text mustBe AgentResultMessages.whyResultHeading
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p1
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason1
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason2
      document.select(Selectors.bullet(1)).text mustBe AgentResultMessages.Out.reason3
      document.select(Selectors.p(1)).text mustBe AgentResultMessages.Out.p2
    }

    "Have the correct Do Next section" in {
      document.select(Selectors.h2(2)).text mustBe InDecisionMessages.doNextHeading
      document.select(Selectors.p(2)).text mustBe AgentResultMessages.Out.doNext
    }

    "Have the correct Download section" in {
      document.select(Selectors.h2(3)).text mustBe UndeterminedDecisionMessages.downloadHeading
      document.select(Selectors.p(3)).text mustBe UndeterminedDecisionMessages.download_p1
    }
  }
}
