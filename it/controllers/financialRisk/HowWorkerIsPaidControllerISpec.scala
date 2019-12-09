package controllers.financialRisk

import helpers.IntegrationSpecBase

class HowWorkerIsPaidControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /how-worker-is-paid" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/how-worker-is-paid")

      whenReady(res) { result =>
         result.status shouldBe OK
        titleOf(result) should include ("How will you be paid for this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/how-worker-is-paid")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/how-worker-is-paid", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("How will you be paid for this work?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      lazy val res = postSessionRequest("/how-worker-is-paid",workerCompValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }

  s"Post or Get to /how-worker-is-paid/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/how-worker-is-paid/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("How will you be paid for this work?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/how-worker-is-paid/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/how-worker-is-paid/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("How will you be paid for this work?")

      }
    }

    "Return a 303 on Successful post and redirect to the Check your answers page with Financial risk" in {

      lazy val res = postSessionRequest("/how-worker-is-paid/change",workerCompValue)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER

      }
    }
  }
}
