package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class IndexControllerISpec extends IntegrationSpecBase {

  s"Get /setup" should {

    "Return a 200 on successful get" in {

      lazy val res = getRequest("/setup", true)

      whenReady(res) { result =>
        result.status shouldBe OK
      }
    }

    "Return a 404 on unsuccessful post" in {

      lazy val res = optionsRequest("/setup")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }

  s"Get /" should {

    "Return a 200 on successful get" in {

      lazy val res = getRequest("/", true)

      whenReady(res) { result =>
        result.status shouldBe OK
      }
    }

    "Return a 404 on unsuccessful post" in {

      lazy val res = optionsRequest("/")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }
  }
}
