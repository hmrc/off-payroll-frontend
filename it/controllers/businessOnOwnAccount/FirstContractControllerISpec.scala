package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class FirstContractControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /first-contract-in-series" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/first-contract-in-series")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Is the current contract the first in a series of contracts agreed with this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/first-contract-in-series")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/first-contract-in-series", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Is the current contract the first in a series of contracts agreed with this client?")

      }
    }

    "Return a 303 on Successful post and redirect to the Contract Extended page" in {

      lazy val res = postSessionRequest("/first-contract-in-series", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /first-contract-in-series/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/first-contract-in-series/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Is the current contract the first in a series of contracts agreed with this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/first-contract-in-series/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/first-contract-in-series/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Is the current contract the first in a series of contracts agreed with this client?")

      }
    }

    "Return a 303 on Successful post and redirect to the Contract Extended page" in {

      lazy val res = postSessionRequest("/first-contract-in-series/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
