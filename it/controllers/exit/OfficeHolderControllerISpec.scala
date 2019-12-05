package controllers.exit

import helpers.IntegrationSpecBase
import models._
import play.api.http.Status
import play.api.libs.json.Json

class OfficeHolderControllerISpec extends IntegrationSpecBase {

  s"Post or Get to /office-holder" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/office-holder")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include("Will you be an ‘Office Holder’?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/office-holder")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/office-holder", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you be an ‘Office Holder’?")

      }
    }

    "Return a 303 on Successful post and move onto next page" in {

      val response = Json.toJson(DecisionResponse(
        "1.6.0",
        "id",
        Score(
          setup = Some(SetupEnum.CONTINUE),
          exit = Some(ExitEnum.CONTINUE)
        ),
        result = ResultEnum.NOT_MATCHED
      ))

      stubPost("/cest-decision/decide", Status.OK, response.toString)

      lazy val res = postSessionRequest("/office-holder", selectedNo)

      whenReady(res) { result =>
        result.status shouldBe SEE_OTHER
        redirectLocation(result) shouldBe Some(controllers.sections.setup.routes.ContractStartedController.onPageLoad(NormalMode).url)
      }
    }
  }

  s"Post or Get to /office-holder/change" should {

    "Return a 200 on successful get and should be on relevant page" in {

      lazy val res = getSessionRequest("/office-holder/change")

      whenReady(res) { result =>
        result.status shouldBe OK
        titleOf(result) should include ("Will you be an ‘Office Holder’?")
      }
    }

    "Return a 404 on a post to unused method" in {

      lazy val res = optionsRequest("/office-holder/change")

      whenReady(res) { result =>
        result.status shouldBe NOT_FOUND
      }
    }

    "Return a 400 on unsuccessful post and stay on the same page" in {

      lazy val res = postSessionRequest("/office-holder/change", defaultValue)

      whenReady(res) { result =>
        result.status shouldBe BAD_REQUEST
        titleOf(result) should include ("Will you be an ‘Office Holder’?")

      }
    }

    "Return a 303 on Successful post as answers not complete" in {

      val response = Json.toJson(DecisionResponse(
        "1.6.0",
        "id",
        Score(
          setup = Some(SetupEnum.CONTINUE),
          exit = Some(ExitEnum.CONTINUE)
        ),
        result = ResultEnum.NOT_MATCHED
      ))

      stubPost("/cest-decision/decide", Status.OK, response.toString)

      lazy val res = postSessionRequest("/office-holder/change", selectedNo)

      whenReady(res) { result =>
      result.status shouldBe SEE_OTHER
      }
    }
  }
}
