package controllers.partAndParcel

import helpers.IntegrationSpecBase
import models.NormalMode
import models.Section.partAndParcel

class BenefitsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /corporate-benefits" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/corporate-benefits")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/corporate-benefits")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/corporate-benefits", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/corporate-benefits", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /corporate-benefits/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/corporate-benefits/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/corporate-benefits/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/corporate-benefits/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will your client provide you with paid-for corporate benefits?")
      }
    }

    "Return a 303 on Successful post and redirect to the check your answers page with part and parcel" in {

      lazy val res = postSessionRequest("/corporate-benefits/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}