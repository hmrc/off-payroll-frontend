package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class ChooseWhereWorkControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /decide-where-work-is-done" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-where-work-is-done")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will your client decide where you do the work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-where-work-is-done")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client decide where you do the work?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done",chooseWhereDoneValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you incur equipment costs that your client will not pay for?")
      }
    }
  }

  s"Post or Get to /decide-where-work-is-done/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-where-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client decide where you do the work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-where-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client decide where you do the work?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done/change",chooseWhereDoneValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
