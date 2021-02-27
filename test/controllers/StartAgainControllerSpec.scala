/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions.FakeIdentifierAction
import play.api.http.Status
import play.api.test.Helpers._
import views.html.StartAgainView

class StartAgainControllerSpec extends ControllerSpecBase {

  val startAgainView = injector.instanceOf[StartAgainView]

  object TestExitSurveyController extends StartAgainController(
    identify = FakeIdentifierAction,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig,
    view = startAgainView,
    checkYourAnswersService = mockCheckYourAnswersService
  )

  "StartAgainController.redirectToExitSurvey" should {

    "Redirect to the ExitSurvey and clear a session" in {
      val result = TestExitSurveyController.redirectToGovUk()(fakeRequest)

      status(result) mustBe Status.SEE_OTHER
      redirectLocation(result) mustBe Some(frontendAppConfig.govUkStartPageUrl)
    }
  }

  "StartAgainController.redirectToDisclaimer" should {

    "Redirect to the redirectToDisclaimer page" in {
      val result = TestExitSurveyController.redirectToDisclaimer()(fakeRequest)

      status(result) mustBe Status.SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax")
    }
  }

  "StartAgainController.somethingWentWrong" should {

    "Render the somethingWentWrong page" in {
      val result = TestExitSurveyController.somethingWentWrong()(fakeRequest)

      status(result) mustBe Status.CONFLICT
    }
  }
}
