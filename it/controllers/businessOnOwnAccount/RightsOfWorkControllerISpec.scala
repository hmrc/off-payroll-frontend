package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import models.{CheckMode, NormalMode}
import play.api.http.Status

class RightsOfWorkControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /client-owns-rights" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-owns-rights")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does the contract state the rights to this work belong to your client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-owns-rights")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-owns-rights", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does the contract state the rights to this work belong to your client?")

      }
    }

    "Return a 303 on Successful post and move onto Buy the Rights page" in {

      lazy val res = postSessionRequest("/client-owns-rights", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.TransferOfRightsController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /client-owns-rights/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-owns-rights/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does the contract state the rights to this work belong to your client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-owns-rights/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-owns-rights/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does the contract state the rights to this work belong to your client?")

      }
    }

    "Return a 303 on Successful post and move onto Buy the Rights page" in {

      lazy val res = postSessionRequest("/client-owns-rights/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.TransferOfRightsController.onPageLoad(CheckMode).url)
      }
    }
  }
}
