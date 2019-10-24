/*
 * Copyright 2019 HM Revenue & Customs
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

import play.api.http.Status
import play.api.test.Helpers._
import views.html.AccessibilityStatementView

class AccessibilityStatementControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[AccessibilityStatementView]
  val dummyPageWithIssue = "/dummy/page/with/problem"

  object TestExitSurveyController extends AccessibilityStatementController(
    controllerComponents = messagesControllerComponents,
    view,
    appConfig = frontendAppConfig
  )

  "AccessibilityStatementController.onPageLoad(pageWithIssue: String)" should {

    "Render the AccessibilityStatementView" in {
      val result = TestExitSurveyController.onPageLoad(dummyPageWithIssue)(fakeRequest)

      status(result) mustBe Status.OK
      contentAsString(result) mustBe view(dummyPageWithIssue).toString()
    }
  }
}
