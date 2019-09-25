package controllers.personalService

import helpers.IntegrationSpecBase

class RejectSubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /could-send-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/could-send-substitute")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does your client have the right to reject a substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/could-send-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/could-send-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to reject a substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/could-send-substitute", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }
  }

  s"Post or Get to /could-send-substitute/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/could-send-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does your client have the right to reject a substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/could-send-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/could-send-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to reject a substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/could-send-substitute/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Would you have to pay your substitute?")
      }
    }
  }
}
