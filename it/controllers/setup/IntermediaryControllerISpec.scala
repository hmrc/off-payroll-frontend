package controllers.setup

import helpers.IntegrationSpecBase

class IntermediaryControllerISpec extends IntegrationSpecBase {

  s"Get to  /worker-intermediary-exit" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-intermediary-exit")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("You need to start again")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest(" /worker-intermediary-exit")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
