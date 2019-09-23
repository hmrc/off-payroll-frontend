package controllers

import helpers.IntegrationSpecBase

class NoIntermediaryControllerISpec extends IntegrationSpecBase {

  s"Get to /worker-no-intermediary-eligibility-exit" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-no-intermediary-eligibility-exit")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Off-payroll working rules (IR35) cannot apply")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-no-intermediary-eligibility-exit")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
