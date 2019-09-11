package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class EquipmentExpensesControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /equipment-expenses" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/equipment-expenses")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you have equipment costs that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/equipment-expenses")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/equipment-expenses", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have equipment costs that your client will not pay for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/equipment-expenses", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have costs for a vehicle that your client will not pay for?")
      }
    }
  }

  s"Post or Get to /equipment-expenses/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/equipment-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have equipment costs that your client will not pay for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/equipment-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/equipment-expenses/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have equipment costs that your client will not pay for?")

      }
    }

    "Return a 409 on Successful post and move onto soemthing went wrong" in {

      lazy val res = postSessionRequest("/equipment-expenses/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
