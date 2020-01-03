/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
