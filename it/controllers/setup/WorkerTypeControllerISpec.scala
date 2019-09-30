package controllers.setup

import helpers.IntegrationSpecBase

class WorkerTypeControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /workers-intermediary" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/workers-intermediary")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/workers-intermediary")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/workers-intermediary", workerTypeValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")

      }
    }
  }

  s"Post or Get to /workers-intermediary/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/workers-intermediary/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/workers-intermediary/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/workers-intermediary/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")

      }
    }
  }
}
