package controllers

import helpers.IntegrationSpecBase

class AccessibilityControllerIISpec extends IntegrationSpecBase {

  s"Get /accessibility-statement" should {

    "Return a 200 on successful get with a query param" in {

      lazy val res = getRequest("/accessibility-statement?problemPageUri=dummy")

      whenReady(res) { result =>
        result.status shouldBe OK
      }
    }

    "Return a 400 on get without a query param" in {

      lazy val res = getRequest("/accessibility-statement")

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
      }
    }
  }
}
