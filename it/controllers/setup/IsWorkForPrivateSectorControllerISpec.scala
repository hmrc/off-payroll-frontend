package controllers.setup

import helpers.IntegrationSpecBase

class IsWorkForPrivateSectorControllerISpec extends IntegrationSpecBase {

  s"Get to /worker-intermediary-eligibility-exit" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-intermediary-eligibility-exit")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Off-payroll working rules might apply to this work")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-intermediary-eligibility-exit")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
