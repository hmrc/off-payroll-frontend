package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class IsWorkForPrivateSectorControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /sector-type" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/sector-type")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("In which sector is the client you will be doing the work for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/sector-type")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/sector-type", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("In which sector is the client you will be doing the work for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/sector-type",selectedYes)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }
  }

  s"Post or Get to /sector-type/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/sector-type/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("In which sector is the client you will be doing the work for?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/sector-type/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/sector-type/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("In which sector is the client you will be doing the work for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/sector-type/change",selectedYes)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }
  }
}
