package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class OtherExpensesControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /other-expenses" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-expenses", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will you incur any other costs that your client will not pay for?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-expenses")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-expenses",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur any other costs that your client will not pay for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/other-expenses",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How will you be paid for this work?")
      }

    }

  }

  s"Post or Get to /other-expenses/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/other-expenses/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will you incur any other costs that your client will not pay for?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/other-expenses/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/other-expenses/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will you incur any other costs that your client will not pay for?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/other-expenses/change",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("How will you be paid for this work?")
      }

    }

  }


}
