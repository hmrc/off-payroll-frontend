package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class PutRightAtOwnCostControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /put-work-right" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/put-work-right", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("If the client was not happy with your work, would you have to put it right?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/put-work-right")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/put-work-right",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("If the client was not happy with your work, would you have to put it right?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/put-work-right",putWorkRightValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client provide you with paid-for corporate benefits?")
      }

    }

  }

  s"Post or Get to /put-work-right/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/put-work-right/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("If the client was not happy with your work, would you have to put it right?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/put-work-right/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/put-work-right/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("If the client was not happy with your work, would you have to put it right?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/put-work-right/change",putWorkRightValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }

    }

  }


}
