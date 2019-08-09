package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class MultipleContractsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  var cookies: Seq[WSCookie] = Nil

  s"Post or Get to /multiple-contracts" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/multiple-contracts", cookies,true)
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/multiple-contracts")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/multiple-contracts",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/wmultiple-contracts",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Disclaimer")
      }

    }

  }

  s"Post or Get to /multiple-contracts/change" should {

    "Get sessionheaders successfully" in {

      lazy val sessionResult = getRequest("/disclaimer", true)

      whenReady(sessionResult) { result =>
        cookies = result.cookies
      }

    }


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/multiple-contracts/change", cookies,true)
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this contract stop you from doing similar work for other clients?")
      }

    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/multiple-contracts/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/multiple-contracts/change",defaultValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this contract stop you from doing similar work for other clients?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/multiple-contracts/change",aboutYouValue, cookies)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Disclaimer")
      }

    }

  }


}
