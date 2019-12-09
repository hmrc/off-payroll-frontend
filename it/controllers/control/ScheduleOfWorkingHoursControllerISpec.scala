package controllers.control

import helpers.IntegrationSpecBase
import models.{NormalMode, Section}

class ScheduleOfWorkingHoursControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /decide-working-hours" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-working-hours")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to decide your working hours?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-working-hours")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-working-hours", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to decide your working hours?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/decide-working-hours",chooseWhenDoneValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /decide-working-hours/change" should {


    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/decide-working-hours/change")
      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to decide your working hours?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/decide-working-hours/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/decide-working-hours/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to decide your working hours?")

      }
    }

    "Return a 303 on Successful post and move onto CheckYourAnswers Page" in {

      lazy val res = postSessionRequest("/decide-working-hours/change",chooseWhenDoneValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
