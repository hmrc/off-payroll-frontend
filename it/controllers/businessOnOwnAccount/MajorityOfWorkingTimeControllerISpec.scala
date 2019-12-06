package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import models.{CheckMode, NormalMode}
import play.api.http.Status

class MajorityOfWorkingTimeControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /majority-of-working-time" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-working-time")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Will this work take up the majority of your available working time?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-working-time")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-working-time", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will this work take up the majority of your available working time?")

      }
    }

    "Return a 303 on Successful post and move onto Similar Work page" in {

      lazy val res = postSessionRequest("/majority-of-working-time", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.SimilarWorkOtherClientsController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /majority-of-working-time/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/majority-of-working-time/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will this work take up the majority of your available working time?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/majority-of-working-time/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/majority-of-working-time/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will this work take up the majority of your available working time?")

      }
    }

    "Return a 303 on Successful post and move onto Similar Work page" in {

      lazy val res = postSessionRequest("/majority-of-working-time/change", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.businessOnOwnAccount.routes.SimilarWorkOtherClientsController.onPageLoad(CheckMode).url)
      }
    }
  }
}
