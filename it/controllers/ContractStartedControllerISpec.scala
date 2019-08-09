package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class ContractStartedControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /work-started" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/work-started", cookies,true)
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

      lazy val res = postSessionRequest("/work-started",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started working for this client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/work-started",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }

    }

  }

  s"Post or Get to /work-started/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/work-started/change", cookies,true)
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

      lazy val res = postSessionRequest("/work-started/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started working for this client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/work-started/change",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you be an ‘Office Holder’?")
      }

    }

  }


}
