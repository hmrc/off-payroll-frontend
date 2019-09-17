package controllers

import helpers.IntegrationSpecBase

class IntermediaryControllerISpec extends IntegrationSpecBase {

  s"Get to /intermediary" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/intermediary")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Off-payroll working rules might apply to this work")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/intermediary")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
