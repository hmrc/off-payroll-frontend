package controllers.businessOnOwnAccount

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import models.{CheckMode, NormalMode}
import play.api.http.Status

class WorkerKnownControllerISpec extends IntegrationSpecBase with CreateRequestHelper with Status with TestData{

  s"Post or Get to /worker-known" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-known")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does your organisation know who will be doing this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-known")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-known", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your organisation know who will be doing this work?")

      }
    }

    "Return a 303 on Successful post and move onto Similar work with Other clients page" in {

      lazy val res = postSessionRequest("/worker-known", selectedYes)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /worker-known/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-known/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does your organisation know who will be doing this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-known/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-known/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your organisation know who will be doing this work?")

      }
    }

    "Return a 303 on Successful post and move onto Similar work with Other clients page" in {

      lazy val res = postSessionRequest("/worker-known/change", selectedYes)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
