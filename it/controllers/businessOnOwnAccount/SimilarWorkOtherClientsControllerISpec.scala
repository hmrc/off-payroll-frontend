package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status

class SimilarWorkOtherClientsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /similar-work" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/similar-work")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Have you done any work for other clients in the last 12 months?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/similar-work")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/similar-work", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you done any work for other clients in the last 12 months?")

      }
    }

//    "Return a 200 on Successful post and move onto next page" in {
//
//      lazy val res = postSessionRequest("/similar-work", selectedNo, followRedirect = false)
//
//      whenReady(res) { result =>
//        result.status shouldBe OK
//        result.body should include ("Are you required to ask permission to work for other clients?")
//      }
//    }
  }

  s"Post or Get to /similar-work/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Have you done any work for other clients in the last 12 months?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/similar-work/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Have you done any work for other clients in the last 12 months?")

      }
    }

//    "Return a 409 on Successful post as answers not complete" in {
//
//      lazy val res = postSessionRequest("/similar-work/change", selectedNo, followRedirect = true)
//
//      whenReady(res) { result =>
//        redirectLocation(result) shouldBe Some("/check-employment-status-for-tax/need-permission/change")
//      }
//    }
  }
}
