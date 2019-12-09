package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class ExtendContractControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /no-similar-work" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/no-similar-work")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does this contract stop you from doing similar work for other clients?")
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
        titleOf(result) should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 303 on Successful post and move onto the Permission to Work with Others page" in {

      lazy val res = postSessionRequest("/no-similar-work", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /no-similar-work/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/no-similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does this contract stop you from doing similar work for other clients?")
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
        titleOf(result) should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 303 on Successful post and move onto the Permission to Work with Others page" in {

      lazy val res = postSessionRequest("/no-similar-work/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
