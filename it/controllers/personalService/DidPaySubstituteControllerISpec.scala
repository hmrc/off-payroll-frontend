package controllers.personalService

import helpers.IntegrationSpecBase
import models.{CheckMode, NormalMode}

class DidPaySubstituteControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /paid-substitute" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/paid-substitute")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Did you pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/paid-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/paid-substitute", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Did you pay your substitute?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/paid-substitute", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.personalService.routes.NeededToPayHelperController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /paid-substitute/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/paid-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Did you pay your substitute?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/paid-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/paid-substitute/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Did you pay your substitute?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/paid-substitute/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.personalService.routes.NeededToPayHelperController.onPageLoad(CheckMode).url)
      }
    }
  }
}
