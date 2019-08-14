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

package views.sections.setup

import assets.messages.ToolNotNeededMessages
import config.SessionKeys
import models.UserType.Worker
import models.{NormalMode, WhichDescribesYouAnswer}
import models.WhichDescribesYouAnswer.{ClientIR35, ClientPAYE}
import play.api.libs.json.Json
import views.behaviours.ViewBehaviours
import views.html.sections.setup.ToolNotNeededView
import play.api.mvc.Request

class ToolNotNeededViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors

  val messageKeyPrefix = "toolNotNeeded.client"

  val view = injector.instanceOf[ToolNotNeededView]

  def createView(clientType: WhichDescribesYouAnswer) = () => view(clientType)(fakeRequest, messages, frontendAppConfig)

  def createViewWithRequest = (userType: WhichDescribesYouAnswer, req: Request[_]) => view(userType)(req, messages, frontendAppConfig)

  "ToolNotNeeded view" when {

    "IR35 user" must {
      behave like normalPage(createView(ClientIR35), messageKeyPrefix + ".ir35", hasSubheading = false)

      behave like pageWithBackLink(createView(ClientIR35))

      lazy val document = asDocument(createViewWithRequest(ClientIR35, workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ToolNotNeededMessages.ir35Title)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ToolNotNeededMessages.ir35Heading
      }

      "have the content messages" in {
        document.select(Selectors.p(1)).text mustBe ToolNotNeededMessages.ir35P1
        document.select(Selectors.p(2)).text mustBe ToolNotNeededMessages.ir35P2
      }
    }

    "PAYE user" must {
      behave like normalPage(createView(ClientPAYE), messageKeyPrefix + ".paye", hasSubheading = false)

      behave like pageWithBackLink(createView(ClientPAYE))

      lazy val document = asDocument(createViewWithRequest(ClientPAYE, workerFakeRequest))

      "have the correct title" in {
        document.title mustBe title(ToolNotNeededMessages.payeTitle)
      }

      "have the correct heading" in {
        document.select(Selectors.heading).text mustBe ToolNotNeededMessages.payeHeading
      }

      "have the content messages" in {
        document.select(Selectors.p(1)).text mustBe ToolNotNeededMessages.payeP1
        document.select(Selectors.p(2)).text mustBe ToolNotNeededMessages.payeP2
      }
    }
  }
}
