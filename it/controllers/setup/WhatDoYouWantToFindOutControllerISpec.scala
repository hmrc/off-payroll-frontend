package controllers.setup

import helpers.IntegrationSpecBase
import models.NormalMode

class WhatDoYouWantToFindOutControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /what-do-you-want-to-find-out" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/what-do-you-want-to-find-out")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("What do you want to find out?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/what-do-you-want-to-find-out")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-find-out", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("What do you want to find out?")

      }
    }

    "Return a 303 on Successful post and redirect to the Who Are You page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-find-out",whatDoYouWantToFindOutValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /what-do-you-want-to-find-out/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/what-do-you-want-to-find-out/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("What do you want to find out?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/what-do-you-want-to-find-out/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/what-do-you-want-to-find-out/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("What do you want to find out?")

      }
    }
  }
}
