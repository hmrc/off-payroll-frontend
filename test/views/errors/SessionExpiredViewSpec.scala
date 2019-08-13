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

package views.errors

import assets.messages.SessionTimeoutMessages
import views.behaviours.ViewBehaviours
import views.html.errors.SessionExpiredView

class SessionExpiredViewSpec extends ViewBehaviours {

  object Selectors extends BaseCSSSelectors {
    val startAgainButton = "a.button"
  }

  val view = injector.instanceOf[SessionExpiredView]

  def createView = () => view(frontendAppConfig)(fakeRequest, messages)

  "Session Expired view" must {
    behave like normalPage(createView, "session.expired", hasSubheading = false)
  }

  "Have a link to the IndexController" in {
    val button = asDocument(createView()).select(Selectors.startAgainButton)
    button.attr("href") mustBe controllers.routes.StartAgainController.redirectToDisclaimer().url
    button.text mustBe SessionTimeoutMessages.startAgain
  }
}
