package controllers.financialRisk

import helpers.IntegrationSpecBase

class EquipmentExpensesControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /equipment-costs" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/equipment-costs")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Will you have to buy equipment before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/equipment-costs")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/equipment-costs", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you have to buy equipment before your client pays you?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/equipment-costs", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will you have to fund any vehicle costs before your client pays you?")
      }
    }
  }

  s"Post or Get to /equipment-costs/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/equipment-costs/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will you have to buy equipment before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/equipment-costs/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/equipment-costs/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you have to buy equipment before your client pays you?")

      }
    }

    "Return a 409 on Successful post and move onto soemthing went wrong" in {

      lazy val res = postSessionRequest("/equipment-costs/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        titleOf(result) should include ("Something went wrong")
      }
    }
  }
}
