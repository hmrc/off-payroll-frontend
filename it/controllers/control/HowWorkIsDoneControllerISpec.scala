package controllers.control

import helpers.IntegrationSpecBase

class HowWorkIsDoneControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /decide-how-work-is-done" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-how-work-is-done")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Does your client have the right to decide how the work is done?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-how-work-is-done")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to decide how the work is done?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done",howWorkDoneValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does your client have the right to decide your working hours?")
      }
    }
  }

  s"Post or Get to /decide-how-work-is-done/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-how-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Does your client have the right to decide how the work is done?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-how-work-is-done/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Does your client have the right to decide how the work is done?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/decide-how-work-is-done/change",howWorkDoneValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
