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

package controllers.sections.financialRisk

import config.featureSwitch.OptimisedFlow
import connectors.mocks.MockDataCacheConnector
import controllers.actions._
import controllers.ControllerSpecBase
import forms.PutRightAtOwnCostFormProvider
import models.Answers._
import models.PutRightAtOwnCost.OutsideOfHoursNoCharge
import models._
import models.requests.DataRequest

import pages.sections.financialRisk.PutRightAtOwnCostPage
import pages.sections.partParcel.BenefitsPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.test.Helpers._
import services.mocks.MockCompareAnswerService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.subOptimised.sections.financialRisk.{PutRightAtOwnCostView => SubOptimisedPutRightAtOwnCostView}
import views.html.sections.financialRisk.PutRightAtOwnCostView

class PutRightAtOwnCostControllerSpec extends ControllerSpecBase {

  val formProvider = new PutRightAtOwnCostFormProvider()
  val form = formProvider()

  val subOptimisedView = injector.instanceOf[SubOptimisedPutRightAtOwnCostView]
  val optimisedView = injector.instanceOf[PutRightAtOwnCostView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PutRightAtOwnCostController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    subOptimisedView = subOptimisedView,
    optimisedView = optimisedView,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = FakeFinancialRiskNavigator,
    frontendAppConfig
  )

  val validData = Map(PutRightAtOwnCostPage.toString -> Json.toJson(Answers(PutRightAtOwnCost.values.head,0)))

  "PutRightAtOwnCost Controller" when {

    "optimal flow is enabled" must {

      def viewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

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

        contentAsString(result) mustBe viewAsString(form.fill(PutRightAtOwnCost.values.head))
      }

      "redirect to the next page when valid data is submitted" in {

        implicit val hc = new HeaderCarrier()
        enable(OptimisedFlow)

        val userAnswers = UserAnswers("id").set(PutRightAtOwnCostPage, 0, OutsideOfHoursNoCharge)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))

        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",userAnswers),PutRightAtOwnCost)(userAnswers)
        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

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
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }

    "optimal flow is disabled" must {

      def viewAsString(form: Form[_] = form) = subOptimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {
        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(PutRightAtOwnCost.values.head))
      }

      "redirect to the next page when valid data is submitted" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))
        val answers = userAnswers.set(PutRightAtOwnCostPage,0, OutsideOfHoursNoCharge)

        mockConstructAnswers(DataRequest(postRequest,"id",answers),PutRightAtOwnCost)(answers)
        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
        mockDecide(answers)(onwardRoute)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER

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
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", PutRightAtOwnCost.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
