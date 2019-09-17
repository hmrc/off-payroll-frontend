package controllers

import helpers.IntegrationSpecBase

class AgentAdvisoryControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /agency-not-responsible" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/agency-not-responsible")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Continue as the worker to check a determination")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/agency-not-responsible")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
