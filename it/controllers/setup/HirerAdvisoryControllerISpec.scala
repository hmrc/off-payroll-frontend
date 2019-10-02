package controllers.setup

import helpers.IntegrationSpecBase

class HirerAdvisoryControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /hirer-advisory" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/hirer-advisory")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Until April 2020, you do not need to determine if this work falls within the off-payroll working rules (IR35)")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/hirer-advisory")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }

}
