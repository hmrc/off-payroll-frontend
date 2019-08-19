package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class BenefitsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /benefits" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/benefits")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/benefits")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/benefits", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client provide you with paid-for corporate benefits?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/benefits", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have any management responsibilities for your client?")
      }
    }
  }

  s"Post or Get to /benefits/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/benefits/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/benefits/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/benefits/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client provide you with paid-for corporate benefits?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/benefits/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
