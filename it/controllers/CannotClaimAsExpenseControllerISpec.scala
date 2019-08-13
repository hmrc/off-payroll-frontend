package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class CannotClaimAsExpenseControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData {

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /worker-cannot-claim" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-cannot-claim", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("What do you have to provide for this engagement that you cannot claim as an expense from the end client or an agency?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-cannot-claim")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-cannot-claim",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("What do you have to provide for this engagement that you cannot claim as an expense from the end client or an agency?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-cannot-claim",cannotClaimValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How will you be paid for this work?")
      }

    }

  }

  s"Post or Get to /worker-cannot-claim/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-cannot-claim/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("What do you have to provide for this engagement that you cannot claim as an expense from the end client or an agency?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-cannot-claim/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-cannot-claim/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("What do you have to provide for this engagement that you cannot claim as an expense from the end client or an agency?")

      }
    }

    "Return a 409 on Successful post and display something went wrong page" in {

      lazy val res = postSessionRequest("/worker-cannot-claim/change",cannotClaimValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }

    }

  }


}
