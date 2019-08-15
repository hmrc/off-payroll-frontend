package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class IdentifyToStakeholdersControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /introduce-worker" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/introduce-worker")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("How would you introduce yourself to your client’s consumers or suppliers?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/introduce-worker")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/introduce-worker", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("How would you introduce yourself to your client’s consumers or suppliers?")

      }
    }

    "Return a 409 on Successful post and go to the something went wrong page" in {

      lazy val res = postSessionRequest("/introduce-worker",introduceValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }

  s"Post or Get to /introduce-worker/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/introduce-worker/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How would you introduce yourself to your client’s consumers or suppliers?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/introduce-worker/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/introduce-worker/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("How would you introduce yourself to your client’s consumers or suppliers?")

      }
    }

    "Return a 409 on Successful post and move onto the something went wrong page" in {

      lazy val res = postSessionRequest("/introduce-worker/change",introduceValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}