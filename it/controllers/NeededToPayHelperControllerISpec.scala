package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class NeededToPayHelperControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /paid-helper" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/paid-helper")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Have you paid another person to do a significant amount of this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/paid-helper")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/paid-helper", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you paid another person to do a significant amount of this work?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/paid-helper", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Can the task be changed without your agreement?")
      }
    }
  }

  s"Post or Get to /paid-helper/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/paid-helper/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you paid another person to do a significant amount of this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/paid-helper/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/paid-helper/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you paid another person to do a significant amount of this work?")

      }
    }

    "Return a 409 on Successful post as no other answers given" in {

      lazy val res = postSessionRequest("/paid-helper/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
