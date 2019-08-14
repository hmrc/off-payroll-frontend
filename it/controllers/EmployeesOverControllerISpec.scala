package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class EmployeesOverControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /private-sector-employees" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-employees")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-employees")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-employees", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation employ more than 50 people?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-employees", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")
      }
    }
  }

  s"Post or Get to /private-sector-employees/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-employees/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-employees/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-employees/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation employ more than 50 people?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-employees/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have more than £5.1 million on its balance sheet?")
      }
    }
  }
}
