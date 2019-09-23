package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class TransferOfRightsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /client-owns-rights" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-owns-rights")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does the contract give your client the option to buy the rights for a separate fee")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-owns-rights")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-owns-rights", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does the contract give your client the option to buy the rights for a separate fee")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/client-owns-rights", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you had a previous contract with this client?")
      }
    }
  }

  s"Post or Get to /client-owns-rights/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-owns-rights/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does the contract give your client the option to buy the rights for a separate fee")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-owns-rights/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-owns-rights/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does the contract give your client the option to buy the rights for a separate fee")

      }
    }

    //TODO - reinstate once navigation is in
//    "Return a 200 on Successful post and move onto next page" in {
//
//      lazy val res = postSessionRequest("/client-owns-rights/change", selectedNo)
//
//      whenReady(res) { result =>
//        result.status shouldBe OK
//        result.body should include ("Disclaimer")
//      }
//
//    }
  }
}
