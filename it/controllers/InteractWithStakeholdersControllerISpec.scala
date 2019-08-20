package controllers

import config.featureSwitch.BusinessOnOwnAccountJourney
import helpers.IntegrationSpecBase

class InteractWithStakeholdersControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /external-interaction" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/external-interaction")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Do you interact with the end client’s customers, clients, audience or users?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/external-interaction")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/external-interaction", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Do you interact with the end client’s customers, clients, audience or users?")

      }
    }

    "Return a 409 on Successful post as no other answers recorded" in {

      disable(BusinessOnOwnAccountJourney)
      lazy val res = postSessionRequest("/external-interaction", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }

  s"Post or Get to /external-interaction/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/external-interaction/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Do you interact with the end client’s customers, clients, audience or users?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/external-interaction/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/external-interaction/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Do you interact with the end client’s customers, clients, audience or users?")

      }
    }

    "Return a 409 on Successful post as no other answers recorded" in {

      lazy val res = postSessionRequest("/external-interaction/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
