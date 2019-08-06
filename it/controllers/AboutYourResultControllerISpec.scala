package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class AboutYourResultControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{


  s"Get /disclaimer" should {

    "Return a 200 on successful get" in {

      lazy val res = getRequest("/disclaimer", true)

      whenReady(res) { result =>
        result.status shouldBe OK
      }

    }

    "Return a 404 on unsuccessful post" in {

      lazy val res = optionsRequest("/disclaimer")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }

    }

    "Return a 200 on unsuccessful post" in {

      lazy val res = postRequest("/disclaimer",defaultValue)

      whenReady(res) { result =>
        result.status shouldBe OK
      }

    }


  }

}
