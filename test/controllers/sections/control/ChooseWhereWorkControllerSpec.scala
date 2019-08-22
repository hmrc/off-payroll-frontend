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

package controllers.sections.control

import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.ChooseWhereWorkFormProvider
import models.Answers._
import models.ChooseWhereWork.WorkerChooses
import models._
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeControlNavigator
import pages.ResultPage
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage}
import play.api.data.Form
import play.api.libs.json._
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.FakeTimestamp
import views.html.subOptimised.sections.control.{ChooseWhereWorkView => SubOptimisedChooseWhereWorkView}
import views.html.sections.control.ChooseWhereWorkView

class ChooseWhereWorkControllerSpec extends ControllerSpecBase with MockDataCacheConnector with FeatureSwitching{

  val formProvider = new ChooseWhereWorkFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val optimisedView = injector.instanceOf[ChooseWhereWorkView]
  val subOptimisedView = injector.instanceOf[SubOptimisedChooseWhereWorkView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ChooseWhereWorkController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    optimisedView = optimisedView,
    subOptimisedView = subOptimisedView,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = FakeControlNavigator
  )

  def viewAsString(form: Form[_] = form) = subOptimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
  def optimisedViewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ChooseWhereWorkPage.toString -> Json.toJson(Answers(ChooseWhereWork.values().head,0)))

  "ChooseWhereWork Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET for optimised view" in {
      enable(OptimisedFlow)
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe optimisedViewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(ChooseWhereWork.values().head))
    }

    "populate the view correctly on a GET when the question has previously been answered for optimised view" in {
      enable(OptimisedFlow)

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe optimisedViewAsString(form.fill(ChooseWhereWork.values().head))
    }

    "redirect to the next page when valid data is submitted" in {
      val answers = userAnswers.set(ChooseWhereWorkPage,0, WorkerChooses)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
      mockDecide(answers)(onwardRoute)

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ChooseWhereWork.options().head.value))

      mockConstructAnswers(DataRequest(postRequest,"id",answers),ChooseWhereWork)(answers)

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data is submitted for optimised view" in {
      enable(OptimisedFlow)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ChooseWhereWork.options().head.value))

      val answers = userAnswers.set(ChooseWhereWorkPage,0, WorkerChooses)
      mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),ChooseWhereWork)(answers)

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted for optimised view" in {

      enable(OptimisedFlow)
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe optimisedViewAsString(boundForm)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ChooseWhereWork.options().head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
