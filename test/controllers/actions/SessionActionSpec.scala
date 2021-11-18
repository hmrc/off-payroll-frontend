/*
 * Copyright 2021 HM Revenue & Customs
 *
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
        redirectLocation(result).get must startWith(controllers.routes.IndexController.onPageLoad.url)

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
