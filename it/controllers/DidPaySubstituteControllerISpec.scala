package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class DidPaySubstituteControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /worker-paid-substitute" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-paid-substitute", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Did you pay your substitute?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-paid-substitute")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-paid-substitute",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Did you pay your substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-paid-substitute",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you paid another person to do a significant amount of this work?")
      }

    }

  }

  s"Post or Get to /worker-paid-substitute/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-paid-substitute/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Did you pay your substitute?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-paid-substitute/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-paid-substitute/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Did you pay your substitute?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-paid-substitute/change",selectedNo, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you paid another person to do a significant amount of this work?")
      }

    }

  }


}
