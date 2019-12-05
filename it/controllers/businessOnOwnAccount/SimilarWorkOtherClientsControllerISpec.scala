package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import models.Section
import play.api.http.Status

class SimilarWorkOtherClientsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /similar-work" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/similar-work")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Have you done any self-employed work of a similar nature for other clients in the last 12 months?")
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
        titleOf(result) should include ("Have you done any self-employed work of a similar nature for other clients in the last 12 months?")

      }
    }

    "Return a 303 on Successful post and move onto review answers" in {

      lazy val res = postSessionRequest("/similar-work", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.routes.CheckYourAnswersController.onPageLoad().url)
      }
    }
  }

  s"Post or Get to /similar-work/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/similar-work/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Have you done any self-employed work of a similar nature for other clients in the last 12 months?")
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
        titleOf(result) should include ("Have you done any self-employed work of a similar nature for other clients in the last 12 months?")

      }
    }

    "Return a 409 on Successful post as answers not complete" in {

      lazy val res = postSessionRequest("/similar-work/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.routes.CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount)).url)
      }
    }
  }
}
