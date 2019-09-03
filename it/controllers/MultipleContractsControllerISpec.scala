package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class MultipleContractsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /multiple-contracts" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/multiple-contracts")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/multiple-contracts")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/multiple-contracts", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/multiple-contracts", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you required to ask permission to work for other clients?")
      }
    }
  }

  s"Post or Get to /multiple-contracts/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/multiple-contracts/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/multiple-contracts/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/multiple-contracts/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/multiple-contracts/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/permission-to-work-with-others/change")
      }
    }
  }
}
