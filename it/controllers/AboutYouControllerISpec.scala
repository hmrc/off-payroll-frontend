package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class AboutYouControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /reason-for-using-tool" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/reason-for-using-tool")
      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("What do you want to find out?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/reason-for-using-tool")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/reason-for-using-tool", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("What do you want to find out?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/reason-for-using-tool", aboutYouValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }
  }

  s"Post or Get to /reason-for-using-tool/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/reason-for-using-tool/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("What do you want to find out?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/reason-for-using-tool/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/reason-for-using-tool/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("What do you want to find out?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/reason-for-using-tool/change", aboutYouValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Are you trading through a limited company, partnership or unincorporated body?")
      }
    }
  }
}
