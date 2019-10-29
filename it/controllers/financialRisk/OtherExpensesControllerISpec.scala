package controllers.financialRisk

import helpers.IntegrationSpecBase

class OtherExpensesControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /other-costs" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-costs")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you have to fund any other costs before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-costs")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-costs", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have to fund any other costs before your client pays you?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/other-costs", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How will you be paid for this work?")
      }
    }
  }

  s"Post or Get to /other-costs/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-costs/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have to fund any other costs before your client pays you?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-costs/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-costs/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you have to fund any other costs before your client pays you?")

      }
    }

    "Return a 409 on Successful post as no other answers given" in {

      lazy val res = postSessionRequest("/other-costs/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
