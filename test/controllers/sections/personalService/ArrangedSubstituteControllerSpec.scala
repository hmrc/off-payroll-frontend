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

package controllers.sections.personalService

import config.featureSwitch.OptimisedFlow
import connectors.FakeDataCacheConnector
import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.ArrangedSubstituteFormProvider
import models.Answers._
import models.requests.DataRequest
import models.{Answers, ArrangedSubstitute, NormalMode}

import pages.sections.personalService.{ArrangedSubstitutePage, DidPaySubstitutePage}
import play.api.data.Form
import play.api.libs.json._
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.personalService.ArrangedSubstituteView
import views.html.subOptimised.sections.personalService.{ArrangedSubstituteView => SubOptimisedArrangedSubstituteView}

class ArrangedSubstituteControllerSpec extends ControllerSpecBase with MockDataCacheConnector {

  val formProvider = new ArrangedSubstituteFormProvider()
  val form = formProvider()

  val optimisedView = injector.instanceOf[ArrangedSubstituteView]
  val subOptimisedView = injector.instanceOf[SubOptimisedArrangedSubstituteView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ArrangedSubstituteController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    optimisedView = optimisedView,
    subOptimisedView = subOptimisedView,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = fakeNavigator
  )

  def viewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ArrangedSubstitutePage.toString -> Json.toJson(Answers(ArrangedSubstitute.values.head, 0)))

  "ArrangedSubstitute Controller" must {

    "For the OptimisedJourney" should {

      "return OK and the correct view for a GET" in {
        enable(OptimisedFlow)
        val result = controller().onPageLoad(NormalMode)(fakeRequest)
        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        enable(OptimisedFlow)
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(ArrangedSubstitute.values.head))
      }

      "redirect to the next page when valid data is submitted" in {
        enable(OptimisedFlow)
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ArrangedSubstitute.options.head.value))

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val answers = userAnswers.set(ArrangedSubstitutePage,0,ArrangedSubstitute.YesClientAgreed)
        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),ArrangedSubstitute)(answers)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }

      "return a Bad Request and errors when invalid data is submitted" in {
        enable(OptimisedFlow)
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = form.bind(Map("value" -> "invalid value"))

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe BAD_REQUEST
        contentAsString(result) mustBe viewAsString(boundForm)
      }

      "redirect to Index Controller for a GET if no existing data is found" in {
        enable(OptimisedFlow)
        val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }

      "redirect to Index Controller for a POST if no existing data is found" in {
        enable(OptimisedFlow)
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ArrangedSubstitute.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }

    "For the SubOptimisedJourney" should {

      def viewAsString(form: Form[_] = form) = subOptimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {

        val result = controller().onPageLoad(NormalMode)(fakeRequest)
        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(ArrangedSubstitute.values.head))
      }

      "redirect to the next page when valid data is submitted" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ArrangedSubstitute.options.head.value))

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val answers = userAnswers.set(ArrangedSubstitutePage,0,ArrangedSubstitute.YesClientAgreed)
        mockConstructAnswers(DataRequest(postRequest,"id",answers),ArrangedSubstitute)(answers)

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

      "redirect to Index Controller for a GET if no existing data is found" in {
        val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }

      "redirect to Index Controller for a POST if no existing data is found" in {
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ArrangedSubstitute.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
