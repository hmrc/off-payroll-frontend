package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class FollowOnContractControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /contract-series" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/contract-series")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will this contract start immediately after the previous one ended?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/contract-series")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/contract-series", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this contract start immediately after the previous one ended?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/contract-series", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Is the current contract the first in a series of contracts agreed with this client?")
      }
    }
  }

  s"Post or Get to /contract-series/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/contract-series/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will this contract start immediately after the previous one ended?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/contract-series/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/contract-series/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this contract start immediately after the previous one ended?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/contract-series/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/first-contract-in-series/change")
      }
    }
  }
}
