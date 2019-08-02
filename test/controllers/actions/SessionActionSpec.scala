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

package controllers.actions

import base.GuiceAppSpecBase
import play.api.mvc.{BaseController, ControllerComponents}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class SessionActionSpec extends GuiceAppSpecBase {

  class Harness(action: IdentifierAction) extends BaseController {
    override def controllerComponents: ControllerComponents = messagesControllerComponents
    def onPageLoad() = action { _ => Ok }
  }

  "Session Action" when {
    "there's no active session" must {
      "redirect to the Index Page page" in {
        val sessionAction = new SessionIdentifierAction(frontendAppConfig, messagesControllerComponents)
        val controller = new Harness(sessionAction)
        val result = controller.onPageLoad()(FakeRequest())
        status(result) mustBe SEE_OTHER
        redirectLocation(result).get must startWith(controllers.routes.IndexController.onPageLoad().url)
      }
    }
    "there's an active session" must {
      "perform the action" in {
        val sessionAction = new SessionIdentifierAction(frontendAppConfig, messagesControllerComponents)
        val controller = new Harness(sessionAction)
        val result = controller.onPageLoad()(fakeRequest)
        status(result) mustBe 200
      }
    }
  }
}
