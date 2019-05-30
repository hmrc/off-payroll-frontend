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

package controllers.sections.setup

import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.FakeDataCacheConnector
import controllers.{ControllerHelper, ControllerSpecBase}
import controllers.actions._
import forms.{WorkerTypeFormProvider, WorkerUsingIntermediaryFormProvider}
import models.Answers._
import models.{Answers, NormalMode, UserAnswers, WorkerType}
import navigation.FakeNavigator
import pages.sections.setup.{WorkerTypePage, WorkerUsingIntermediaryPage}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.WorkerUsingIntermediaryView
import views.html.subOptimised.sections.setup.WorkerTypeView
import connectors.FakeDataCacheConnector
import connectors.mocks.MockDataCacheConnector
import services.mocks.MockCompareAnswerService

class WorkerTypeControllerSpec extends ControllerSpecBase with FeatureSwitching with MockDataCacheConnector with MockCompareAnswerService {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new WorkerTypeFormProvider()
  val formProviderInt = new WorkerUsingIntermediaryFormProvider()
  val form = formProvider()
  val formInt = formProviderInt()

  val view = injector.instanceOf[WorkerTypeView]
  val viewInt = injector.instanceOf[WorkerUsingIntermediaryView]

  val mockControllerHelper = new ControllerHelper(mockCompareAnswerService,mockDataCacheConnector, new FakeNavigator(onwardRoute),messagesControllerComponents,mockDecisionService)

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) =
    new WorkerTypeController(
      FakeIdentifierAction,
      dataRetrievalAction,
      new DataRequiredActionImpl(messagesControllerComponents),
      formProvider,
      formProviderInt,
      messagesControllerComponents,
      view,
      viewInt,
      mockControllerHelper,
      frontendAppConfig
    )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
  def viewAsStringInt(form: Form[_] = formInt) = viewInt(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WorkerTypePage.toString -> Json.toJson(Answers(WorkerType.values.head,0)))
  val validDataInt = Map(WorkerUsingIntermediaryPage.toString -> Json.toJson(Answers(true, 0)))

  "WorkerType Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET in the optimised view" in {

      enable(OptimisedFlow)
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsStringInt()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(WorkerType.values.head))
    }

    "populate the view correctly on a GET when the question has previously been answered for the optimised flow" in {

      enable(OptimisedFlow)
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validDataInt)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsStringInt(formInt.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WorkerType.options.head.value))

      val userAnswers = UserAnswers("id")
      mockConstructAnswers(userAnswers)(userAnswers)
      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data is submitted for the optimised flow" in {

      enable(OptimisedFlow)
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val userAnswers = UserAnswers("id")
      mockConstructAnswers(userAnswers)(userAnswers)
      mockSave(CacheMap(cacheMapId, validDataInt))(CacheMap(cacheMapId, validDataInt))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "return a Bad Request and errors when invalid data is submitted for the optimised flow" in {
      enable(OptimisedFlow)

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = formInt.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsStringInt(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WorkerType.options.head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
