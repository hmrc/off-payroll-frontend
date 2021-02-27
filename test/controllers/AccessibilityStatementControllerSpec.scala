/*
 * Copyright 2021 HM Revenue & Customs
 *
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
