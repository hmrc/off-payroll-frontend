package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class TransferOfRightsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /transfer-of-rights" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/transfer-of-rights")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does the contract give the client the option to buy the rights for a separate fee?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/transfer-of-rights")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/transfer-of-rights", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does the contract give the client the option to buy the rights for a separate fee?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/transfer-of-rights", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Disclaimer")
      }
    }
  }

  s"Post or Get to /transfer-of-rights/change" should {

    //TODO - reinstate once navigation is in
//    "Return a 200 on successful get and should be on relevant page" in {
//
//      lazy val res = getSessionRequest("/transfer-of-rights/change")
//      whenReady(res) { result =>
//        result.status shouldBe OK
//        result.body should include ("Does the contract give the client the option to buy the rights for a separate fee?")
//      }
//
//    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/transfer-of-rights/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/transfer-of-rights/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does the contract give the client the option to buy the rights for a separate fee?")

      }
    }

    //TODO - reinstate once navigation is in
//    "Return a 200 on Successful post and move onto next page" in {
//
//      lazy val res = postSessionRequest("/transfer-of-rights/change", selectedNo)
//
//      whenReady(res) { result =>
//        result.status shouldBe OK
//        result.body should include ("Disclaimer")
//      }
//
//    }
  }
}
