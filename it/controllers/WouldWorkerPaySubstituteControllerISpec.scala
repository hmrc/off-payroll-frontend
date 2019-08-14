package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class WouldWorkerPaySubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /worker-would-pay-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-would-pay-substitute")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-would-pay-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-would-pay-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Would you have to pay your substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-would-pay-substitute", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Can the task be changed without your agreement?")
      }
    }
  }

  s"Post or Get to /worker-would-pay-substitute/change" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-would-pay-substitute/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-would-pay-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-would-pay-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Would you have to pay your substitute?")

      }
    }

    "Return a 409 on Successful post as check your answers is not complete" in {

      lazy val res = postSessionRequest("/worker-would-pay-substitute/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
