package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class BenefitsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /benefits" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/benefits", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will your client provide you with paid-for corporate benefits?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/benefits")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/benefits",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client provide you with paid-for corporate benefits?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/benefits",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have any management responsibilities for your client?")
      }

    }

  }

  s"Post or Get to /benefits/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/benefits/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client provide you with paid-for corporate benefits?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/benefits/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/benefits/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client provide you with paid-for corporate benefits?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/benefits/change",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you have any management responsibilities for your client?")
      }

    }

  }


}
