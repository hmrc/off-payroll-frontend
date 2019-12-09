package controllers.financialRisk

import helpers.IntegrationSpecBase
import models.NormalMode
import models.Section.financialRisk

class PutRightAtOwnCostControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /put-work-right" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/put-work-right")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("If the client was not happy with your work, would you have to put it right?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/put-work-right")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/put-work-right", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("If the client was not happy with your work, would you have to put it right?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/put-work-right",putWorkRightValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /put-work-right/change" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/put-work-right/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("If the client was not happy with your work, would you have to put it right?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/put-work-right/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/put-work-right/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("If the client was not happy with your work, would you have to put it right?")

      }
    }

    "Return a 409 on Successful post and redirect to the check your answers page with financial risk" in {

      lazy val res = postSessionRequest("/put-work-right/change",putWorkRightValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
