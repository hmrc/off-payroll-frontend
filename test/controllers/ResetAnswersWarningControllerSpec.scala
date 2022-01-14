/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import assets.messages.ResetAnswersMessages
import controllers.actions._
import forms.ResetAnswersWarningFormProvider
import play.api.test.Helpers._
import views.html.ResetAnswersWarningView

class ResetAnswersWarningControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[ResetAnswersWarningView]
  val formProvider = new ResetAnswersWarningFormProvider()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ResetAnswersWarningController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    formProvider = formProvider
  )

  "ResetAnswersWarning Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      titleOf(result) mustBe title(ResetAnswersMessages.title)
    }

    "redirect to home when passed true" in {
      val result = controller().onSubmit(fakeRequest.withFormUrlEncodedBody(("value", "true")))
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax")
    }

    "redirect back to CYA when passed false" in {
      val result = controller().onSubmit(fakeRequest.withFormUrlEncodedBody(("value", "false")))
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/review-your-answers")
    }
  }
}




