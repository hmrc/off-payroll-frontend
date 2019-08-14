package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class RejectSubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /substitute-rejected" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/substitute-rejected")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/substitute-rejected")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/substitute-rejected", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/substitute-rejected", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }
  }

  s"Post or Get to /substitute-rejected/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/substitute-rejected/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/substitute-rejected/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/substitute-rejected/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/substitute-rejected/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }
  }
}
