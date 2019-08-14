package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class BalanceSheetOverControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /private-sector-balance-sheet" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-balance-sheet")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-balance-sheet")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-balance-sheet", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-balance-sheet", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }
  }

  s"Post or Get to /private-sector-balance-sheet/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-balance-sheet/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-balance-sheet/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-balance-sheet/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-balance-sheet/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }
  }
}
