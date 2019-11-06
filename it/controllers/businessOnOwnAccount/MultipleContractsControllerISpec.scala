package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class MultipleContractsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /no-similar-work" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/no-similar-work")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/no-similar-work")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/no-similar-work", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/no-similar-work", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you required to ask permission to work for other clients?")
      }
    }
  }

  s"Post or Get to /no-similar-work/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/no-similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/no-similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/no-similar-work/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/no-similar-work/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/need-permission/change")
      }
    }
  }
}
