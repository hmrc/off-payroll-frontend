package controllers

import helpers.{CreateRequestHelper, IntegrationSpecBase, TestData}
import play.api.http.Status
import play.api.libs.ws.WSCookie

class ScheduleOfWorkingHoursControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /decide-working-schedule" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-working-schedule")

      whenReady(res) { result =>
         result.status shouldBe OK
        result.body should include ("Will your client decide the working hours?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-working-schedule")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-working-schedule", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client decide the working hours?")

      }
    }

    "Return a 200 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-working-schedule",chooseWhenDoneValue)

      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client decide where you do the work?")
      }
    }
  }

  s"Post or Get to /decide-working-schedule/change" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-working-schedule/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        result.body should include ("Will your client decide the working hours?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-working-schedule/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-working-schedule/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        result.body should include ("Will your client decide the working hours?")

      }
    }

    "Return a 409 on Successful post and move onto something went wrong" in {

      lazy val res = postSessionRequest("/decide-working-schedule/change",chooseWhenDoneValue)

      whenReady(res) { result =>
        result.status shouldBe CONFLICT
        result.body should include ("Something went wrong")
      }
    }
  }
}
