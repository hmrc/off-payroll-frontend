package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class OfficeHolderControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /office-holder-duties" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/office-holder-duties")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/office-holder-duties")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/office-holder-duties", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you be an ‘Office Holder’?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/office-holder-duties", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started working for this client?")
      }
    }
  }

  s"Post or Get to /office-holder-duties/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/office-holder-duties/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/office-holder-duties/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/office-holder-duties/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you be an ‘Office Holder’?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/office-holder-duties/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
      }
    }
  }
}
