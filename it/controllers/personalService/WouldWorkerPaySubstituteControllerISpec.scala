package controllers.personalService

import helpers.IntegrationSpecBase
import models.NormalMode

class WouldWorkerPaySubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /would-pay-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/would-pay-substitute")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Would you have to pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/would-pay-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/would-pay-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Would you have to pay your substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/would-pay-substitute", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.control.routes.MoveWorkerController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /would-pay-substitute/change" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/would-pay-substitute/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Would you have to pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/would-pay-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/would-pay-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Would you have to pay your substitute?")

      }
    }

    "Return a 303 on Successful post as check your answers is not complete" in {

      lazy val res = postSessionRequest("/would-pay-substitute/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
      }
    }
  }
}
