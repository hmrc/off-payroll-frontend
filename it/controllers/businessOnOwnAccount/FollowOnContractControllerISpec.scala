package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class FollowOnContractControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /contract-series" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/contract-series")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Will this contract start immediately after the previous one ended?")
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
        titleOf(result) should include ("Will this contract start immediately after the previous one ended?")

      }
    }

    "Return a 303 on Successful post and move onto First in Series question" in {

      lazy val res = postSessionRequest("/contract-series", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /contract-series/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/contract-series/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will this contract start immediately after the previous one ended?")
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
        titleOf(result) should include ("Will this contract start immediately after the previous one ended?")

      }
    }

    "Return a 303 on Successful post and move onto First in Series question" in {

      lazy val res = postSessionRequest("/contract-series/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
