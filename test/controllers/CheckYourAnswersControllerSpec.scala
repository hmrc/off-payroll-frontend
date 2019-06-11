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

import assets.messages.CheckYourAnswersMessages
import controllers.actions._
import handlers.ErrorHandler
import models.requests.DataRequest
import models.{DecisionResponse, ErrorResponse, ResultEnum, Score}
import navigation.FakeNavigator
import pages.ResultPage
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html
import services.{CheckYourAnswersService, CompareAnswerService}
import utils.FakeTimestamp
import views.html.CheckYourAnswersView

class CheckYourAnswersControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[CheckYourAnswersView]
  val mockCheckAnswerService = app.injector.instanceOf[CheckYourAnswersService]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new CheckYourAnswersController(
    new FakeNavigator(onwardRoute),
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    mockCheckAnswerService,
    mockOptimisedDecisionService,
    mockErrorHandler,
    frontendAppConfig
  )

  "CheckYourAnswers Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      titleOf(result) mustBe title(CheckYourAnswersMessages.title)
    }

    "redirect to the result page when inside ir35 is submitted" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.INSIDE_IR35)

      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/your-result")
    }

    "redirect to the result page when outside ir35 is submitted" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.OUTSIDE_IR35)

      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/your-result")
    }

    "redirect to the result page when self employed is submitted" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.SELF_EMPLOYED)

      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/your-result")
    }

    "redirect to the result page when unknown is submitted" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.UNKNOWN)

      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/your-result")
    }

    "redirect to the result page when employed is submitted" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.EMPLOYED)

      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/check-employment-status-for-tax/your-result")
    }

    "redirect to error page if not matched" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())
      val decision = DecisionResponse("","",Score(None,None,None,None,None,None),ResultEnum.NOT_MATCHED)

      mockInternalServerError(Html("ma nem Jeff"))
      mockMultipleCall(DataRequest(postRequest,"id",answers))(Right(decision))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to error page if error occurs" in {
      val postRequest = fakeRequest
      val answers = userAnswers.set(ResultPage,0,FakeTimestamp.timestamp())

      mockInternalServerError(Html("ma nem Jeff"))
      mockMultipleCall(DataRequest(postRequest,"id",answers))(Left(ErrorResponse(INTERNAL_SERVER_ERROR,"Wrong name detected")))
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to Index for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index for a POST if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}




