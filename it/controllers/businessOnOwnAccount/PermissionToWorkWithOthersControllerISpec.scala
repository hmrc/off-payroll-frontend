package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class PermissionToWorkWithOthersControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /need-permission" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/need-permission")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Are you required to ask permission to work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/need-permission")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/need-permission", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Are you required to ask permission to work for other clients?")

      }
    }

    "Return a 303 on Successful post and move onto the Ownership Rights page" in {

      lazy val res = postSessionRequest("/need-permission", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /need-permission/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/need-permission/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Are you required to ask permission to work for other clients?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/need-permission/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/need-permission/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Are you required to ask permission to work for other clients?")

      }
    }

    "Return a 303 on Successful post and move onto the Ownership Rights page" in {

      lazy val res = postSessionRequest("/need-permission/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
