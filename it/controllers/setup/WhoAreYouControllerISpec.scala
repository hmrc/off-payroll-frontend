package controllers.setup

import helpers.IntegrationSpecBase
import models.NormalMode

class WhoAreYouControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /who-are-you" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/who-are-you")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Who are you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/who-are-you")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/who-are-you", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Who are you?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/who-are-you", whoAreYouValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
      }
    }
  }

  s"Post or Get to /who-are-you/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/who-are-you/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Who are you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/who-are-you/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/who-are-you/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Who are you?")

      }
    }
  }
}
