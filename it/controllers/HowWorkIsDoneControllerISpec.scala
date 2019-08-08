package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class HowWorkIsDoneControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /decide-how-work-is-done" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-how-work-is-done", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Have you already started this particular engagement for the end client?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-how-work-is-done")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started this particular engagement for the end client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }

    }

  }

  s"Post or Get to /decide-how-work-is-done/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-how-work-is-done/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you already started this particular engagement for the end client?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-how-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you already started this particular engagement for the end client?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done/change",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }

    }

  }


}
