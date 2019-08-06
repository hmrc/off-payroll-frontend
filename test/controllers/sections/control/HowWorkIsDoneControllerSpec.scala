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

import config.featureSwitch.OptimisedFlow
import connectors.FakeDataCacheConnector
import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.HowWorkIsDoneFormProvider
import models.Answers._
import models.requests.DataRequest
import models._
import navigation.FakeNavigator
import pages.sections.control.{HowWorkIsDonePage, MoveWorkerPage}
import pages.sections.setup.AboutYouPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.control.HowWorkIsDoneView
import views.html.subOptimised.sections.control.{HowWorkIsDoneView => SubOptimisedHowWorkIsDoneView}

class HowWorkIsDoneControllerSpec extends ControllerSpecBase with MockDataCacheConnector {

  val formProvider = new HowWorkIsDoneFormProvider()
  val form = formProvider()

  val optimisedView = injector.instanceOf[HowWorkIsDoneView]
  val subOptimisedView = injector.instanceOf[SubOptimisedHowWorkIsDoneView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new HowWorkIsDoneController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig,
    optimisedView = optimisedView,
    subOptimisedView = subOptimisedView,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = fakeNavigator
  )

  def viewAsString(form: Form[_] = form) = subOptimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
  def optimisedViewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(HowWorkIsDonePage.toString -> Json.toJson(Answers(HowWorkIsDone.values().head,0)))

  "HowWorkIsDone Controller" must {

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

      contentAsString(result) mustBe viewAsString(form.fill(HowWorkIsDone.values().head))
    }

    "populate the view correctly on a GET when the question has previously been answered for optimised view" in {

      enable(OptimisedFlow)
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe optimisedViewAsString(form.fill(HowWorkIsDone.values().head))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", HowWorkIsDone.options().head.value))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val answers = userAnswers.set(HowWorkIsDonePage,0,HowWorkIsDone.NoWorkerInputAllowed)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),HowWorkIsDone)(answers)

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data is submitted for optimised view" in {

      enable(OptimisedFlow)
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", HowWorkIsDone.options().head.value))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val answers = userAnswers.set(HowWorkIsDonePage,0,HowWorkIsDone.NoWorkerInputAllowed)
      mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),HowWorkIsDone)(answers)

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

    "return a Bad Request and errors when invalid data is submitted for optimised view" in {

      enable(OptimisedFlow)
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe optimisedViewAsString(boundForm)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", HowWorkIsDone.options().head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
