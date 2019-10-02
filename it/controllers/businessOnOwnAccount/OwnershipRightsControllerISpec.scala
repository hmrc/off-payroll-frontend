package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class OwnershipRightsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /ownership-rights" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/ownership-rights")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Are there any ownership rights relating to this contract?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/ownership-rights")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/ownership-rights", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are there any ownership rights relating to this contract?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/ownership-rights", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you had a previous contract with this client?")
      }
    }
  }

  s"Post or Get to /ownership-rights/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/ownership-rights/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are there any ownership rights relating to this contract?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/ownership-rights/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/ownership-rights/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are there any ownership rights relating to this contract?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/ownership-rights/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/previous-contract/change")
      }
    }
  }
}
