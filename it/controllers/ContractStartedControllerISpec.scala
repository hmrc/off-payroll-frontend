package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class ContractStartedControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /work-started" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/work-started")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/work-started")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/work-started", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started working for this client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/work-started", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }
    }
  }

  s"Post or Get to /work-started/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/work-started/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/work-started/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/work-started/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started working for this client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/work-started/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }
    }
  }
}
