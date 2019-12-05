package controllers.personalService

import helpers.IntegrationSpecBase

class RejectSubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /right-to-reject-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/right-to-reject-substitute")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to reject a substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/right-to-reject-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/right-to-reject-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to reject a substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/right-to-reject-substitute", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Would you have to pay your substitute?")
      }
    }
  }

  s"Post or Get to /right-to-reject-substitute/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/right-to-reject-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to reject a substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/right-to-reject-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/right-to-reject-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to reject a substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/right-to-reject-substitute/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Would you have to pay your substitute?")
      }
    }
  }
}
