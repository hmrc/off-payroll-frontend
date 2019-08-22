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

package controllers.sections.partParcel

import akka.util.ByteString
import config.featureSwitch.OptimisedFlow
import connectors.mocks.MockDataCacheConnector
import controllers.actions._
import controllers.ControllerSpecBase
import forms.IdentifyToStakeholdersFormProvider
import models.Answers._
import models.IdentifyToStakeholders.WorkForEndClient
import models._
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakePartAndParcelNavigator
import org.mockito.Matchers
import org.mockito.Mockito.when
import pages.sections.partParcel.{IdentifyToStakeholdersPage, InteractWithStakeholdersPage}
import pages.sections.personalService.WouldWorkerPaySubstitutePage
import play.api.data.Form
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc.{Call, ResponseHeader, Result}
import play.api.test.Helpers._
import services.mocks.MockCompareAnswerService
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.partParcel.IdentifyToStakeholdersView
import views.html.subOptimised.sections.partParcel.{IdentifyToStakeholdersView => SubOptimisedIdentifyToStakeholdersView}

import scala.concurrent.Future

class IdentifyToStakeholdersControllerSpec extends ControllerSpecBase {

  val formProvider = new IdentifyToStakeholdersFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val optimisedView = injector.instanceOf[IdentifyToStakeholdersView]
  val subOptimisedView = injector.instanceOf[SubOptimisedIdentifyToStakeholdersView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new IdentifyToStakeholdersController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    optimisedView = optimisedView,
    subOptimisedView = subOptimisedView,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = FakePartAndParcelNavigator,
    frontendAppConfig
  )

  val validData = Map(IdentifyToStakeholdersPage.toString -> Json.toJson(Answers(IdentifyToStakeholders.values(true).head,0)))

  "IdentifyToStakeholders Controller" must {

    "If the OptimisedFlow is enabled" should {

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

        val userAnswers = UserAnswers("id").set(IdentifyToStakeholdersPage,0,IdentifyToStakeholders.WorkForEndClient)
        contentAsString(result) mustBe viewAsString(form.fill(IdentifyToStakeholders.values(true).head))
      }

      "redirect to the next page when valid data is submitted" in {
        enable(OptimisedFlow)

        val userAnswers = UserAnswers("id").set(IdentifyToStakeholdersPage, 0, WorkForEndClient)
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", IdentifyToStakeholders.options.head.value))

        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",userAnswers),IdentifyToStakeholders)(userAnswers)
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

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", IdentifyToStakeholders.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }

    "If the OptimisedFlow is disabled" should {

      def viewAsString(form: Form[_] = form) = subOptimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {
        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {
        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(IdentifyToStakeholders.values().head))
      }

      "redirect to the next page when valid data is submitted" in {

        implicit val hc = new HeaderCarrier()

        val userAnswers = UserAnswers("id").set(IdentifyToStakeholdersPage, 0, WorkForEndClient)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
        mockDecide(userAnswers)(onwardRoute)

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", IdentifyToStakeholders.options.head.value))
        mockConstructAnswers(DataRequest(postRequest,"id",userAnswers),IdentifyToStakeholders)(userAnswers)
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
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", IdentifyToStakeholders.options.head.value))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
