package controllers.setup

import helpers.IntegrationSpecBase

class WorkerAdvisoryControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /worker-advisory" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-advisory")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("You don’t need to determine if the off-payroll rules (IR35) apply to this work")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-advisory")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }

}
