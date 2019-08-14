package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class TurnoverOverControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /turnover-over" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/turnover-over")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/turnover-over")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/turnover-over", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/turnover-over", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }
  }

  s"Post or Get to /turnover-over/edit" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/turnover-over/edit")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/turnover-over/edit")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/turnover-over/edit", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/turnover-over/edit", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }
  }


  s"Post or Get to /private-sector-turnover" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-turnover")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-turnover")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-turnover", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-turnover", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }
  }

  s"Post or Get to /private-sector-turnover/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/private-sector-turnover/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/private-sector-turnover/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/private-sector-turnover/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does this organisation have an annual turnover of more than £10.2 million?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/private-sector-turnover/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does this organisation employ more than 50 people?")
      }
    }
  }
}
