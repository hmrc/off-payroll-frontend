package controllers.control

import helpers.IntegrationSpecBase
import models.{CheckMode, NormalMode, Section}

class ChooseWhereWorkControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /decide-where-work-is-done" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-where-work-is-done")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to decide where you do the work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-where-work-is-done")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to decide where you do the work?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done",chooseWhereDoneValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /decide-where-work-is-done/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-where-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to decide where you do the work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-where-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to decide where you do the work?")

      }
    }

    "Return a 309 on Successful post and move onto CheckYourAnswers Page" in {

      lazy val res = postSessionRequest("/decide-where-work-is-done/change",chooseWhereDoneValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
