package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class PreviousContractControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /previous-contract" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/previous-contract")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Have you had a previous contract with this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/previous-contract")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/previous-contract", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Have you had a previous contract with this client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/previous-contract", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Is the current contract the first in a series of contracts agreed with this client?")
      }
    }
  }

  s"Post or Get to /previous-contract/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/previous-contract/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Have you had a previous contract with this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/previous-contract/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/previous-contract/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Have you had a previous contract with this client?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/previous-contract/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/first-contract-in-series/change")
      }
    }
  }
}
