package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class MaterialsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /material-expenses" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/material-expenses")
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you incur costs for materials that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/material-expenses")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/material-expenses", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur costs for materials that your client will not pay for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/material-expenses", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you incur any other costs that your client will not pay for?")
      }
    }
  }

  s"Post or Get to /material-expenses/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/material-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you incur costs for materials that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/material-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/material-expenses/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur costs for materials that your client will not pay for?")

      }
    }

    "Return a 409 on Successful post as no other answers given" in {

      lazy val res = postSessionRequest("/material-expenses/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
