package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class OtherExpensesControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /other-expenses" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-expenses")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you incur any other costs that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-expenses")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-expenses", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur any other costs that your client will not pay for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/other-expenses", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How will you be paid for this work?")
      }
    }
  }

  s"Post or Get to /other-expenses/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you incur any other costs that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-expenses/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur any other costs that your client will not pay for?")

      }
    }

    "Return a 409 on Successful post as no other answers given" in {

      lazy val res = postSessionRequest("/other-expenses/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
