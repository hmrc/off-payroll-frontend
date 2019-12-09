package controllers.control

import helpers.IntegrationSpecBase
import models.{NormalMode, Section}

class MoveWorkerControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /worker-move-task" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-move-task")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include("Does your client have the right to move you from the task you originally agreed to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-move-task")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-move-task", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include("Does your client have the right to move you from the task you originally agreed to do?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/worker-move-task", taskChangeValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /worker-move-task/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/worker-move-task/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Does your client have the right to move you from the task you originally agreed to do?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/worker-move-task/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/worker-move-task/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Does your client have the right to move you from the task you originally agreed to do?")

      }
    }

    "Return a 303 on Successful post and move onto CheckYourAnswers Page" in {

      lazy val res = postSessionRequest("/worker-move-task/change",taskChangeValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
