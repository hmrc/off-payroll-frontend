package controllers.businessOnOwnAccount

import helpers.IntegrationSpecBase
import models.{CheckMode, NormalMode}

class TransferOfRightsControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /client-buys-rights" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-buys-rights")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does the contract give your client the option to buy the rights for a separate fee")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-buys-rights")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-buys-rights", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does the contract give your client the option to buy the rights for a separate fee")

      }
    }

    "Return a 303 on Successful post and move onto Previous Contract page" in {

      lazy val res = postSessionRequest("/client-buys-rights", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.PreviousContractController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /client-buys-rights/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/client-buys-rights/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does the contract give your client the option to buy the rights for a separate fee")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/client-buys-rights/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/client-buys-rights/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does the contract give your client the option to buy the rights for a separate fee")

      }
    }

    "Return a 303 on Successful post and move onto Previous Contract page" in {

      lazy val res = postSessionRequest("/client-buys-rights/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.PreviousContractController.onPageLoad(CheckMode).url)
      }
    }
  }
}
