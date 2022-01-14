/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions.FakeIdentifierAction
import play.api.http.Status
import play.api.test.Helpers._

class ExitSurveyControllerSpec extends ControllerSpecBase {

  object TestExitSurveyController extends ExitSurveyController(
    identify = FakeIdentifierAction,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig
  )

  "ExitSurveyController.redirectToExitSurvey" should {

    "Redirect to the ExitSurvey and clear a session" in {
      val result = TestExitSurveyController.redirectToExitSurvey()(fakeRequest)

      status(result) mustBe Status.SEE_OTHER
      redirectLocation(result) mustBe Some(frontendAppConfig.exitSurveyUrl)
    }
  }
}
