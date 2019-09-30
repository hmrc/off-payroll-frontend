package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class FinanciallyDependentControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /majority-of-income" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-income")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will this contract provide you with the majority of your income?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-income")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-income", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this contract provide you with the majority of your income?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/majority-of-income", selectedYes)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you done any work for other clients in the last 12 months?")
      }
    }
  }

  s"Post or Get to /majority-of-income/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-income/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will this contract provide you with the majority of your income?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-income/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-income/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will this contract provide you with the majority of your income?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/majority-of-income/change", selectedNo, followRedirect = false)

      whenReady(res) { result =>
        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/similar-work/change")
      }
    }
  }
}
