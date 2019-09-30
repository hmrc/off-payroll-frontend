package controllers.setup

import helpers.IntegrationSpecBase

class ToolNotNeededControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /tool-not-needed" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/tool-not-needed")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("You don’t need to determine if this work should be classed as employed or self-employed for tax purposes")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/tool-not-needed")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

  }

}
