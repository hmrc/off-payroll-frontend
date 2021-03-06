package controllers.setup

import helpers.IntegrationSpecBase

class WorkerUsingIntermediaryControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /workers-intermediary" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/workers-intermediary")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Do you provide your services through a limited company, partnership or unincorporated association?")
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
        titleOf(result) should include ("Do you provide your services through a limited company, partnership or unincorporated association?")

      }
    }
  }

  s"Post or Get to /workers-intermediary/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/workers-intermediary/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Do you provide your services through a limited company, partnership or unincorporated association?")
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
        titleOf(result) should include ("Do you provide your services through a limited company, partnership or unincorporated association?")

      }
    }
  }
}
