package controllers

import helpers.IntegrationSpecBase

class ArrangedSubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /sent-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/sent-substitute")
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Have you ever sent a substitute to do your work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/sent-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/sent-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you ever sent a substitute to do your work?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/sent-substitute",arrangeSubValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")
      }
    }
  }

  s"Post or Get to /sent-substitute/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/sent-substitute/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you ever sent a substitute to do your work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/sent-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/sent-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you ever sent a substitute to do your work?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/sent-substitute/change",arrangeSubValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("If you sent a substitute, who met all your client’s criteria, does the client have the right to reject them?")
      }
    }
  }
}
