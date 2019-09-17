package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class WhoAreYouControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /who-are-you" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/who-are-you")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Who are you?")
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
        result.body should include ("Who are you?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/who-are-you",whoAreYouValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("What do you want to find out?")
      }
    }
  }

  s"Post or Get to /who-are-you/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/who-are-you/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Who are you?")
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
        result.body should include ("Who are you?")

      }
    }
  }
}
