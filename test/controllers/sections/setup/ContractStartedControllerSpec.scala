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

import akka.util.ByteString
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.mocks.MockDataCacheConnector
import controllers.{ControllerHelper, ControllerSpecBase}
import controllers.actions._
import forms.ContractStartedFormProvider
import models.{Answers, NormalMode, UserAnswers}
import org.mockito.Matchers
import org.mockito.Mockito.when
import pages.sections.setup.ContractStartedPage
import play.api.data.Form
import play.api.http.HttpEntity
import play.api.libs.json.Json
import play.api.mvc.{Call, ResponseHeader, Result}
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.subOptimised.sections.setup.ContractStartedView

import scala.concurrent.Future
class ContractStartedControllerSpec extends ControllerSpecBase with MockDataCacheConnector with FeatureSwitching {

  val formProvider = new ContractStartedFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[ContractStartedView]
  val optimisedView = injector.instanceOf[views.html.sections.setup.ContractStartedView]


  val mockControllerHelper = mock[ControllerHelper]
  def onwardRoute = Call("POST", "/foo")

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ContractStartedController(
    appConfig = frontendAppConfig,
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    optimisedView = optimisedView,
    controllerHelper = mockControllerHelper
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
  def viewAsStringOptimised(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ContractStartedPage.toString -> Json.toJson(Answers(true,0)))

  "ContractStarted Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET for the optimised flow" in {
      enable(OptimisedFlow)
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsStringOptimised()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "populate the view correctly on a GET when the question has previously been answered for optimised flow" in {
      enable(OptimisedFlow)
      val validData = Map(ContractStartedPage.toString -> Json.toJson(Answers(true,0)))
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsStringOptimised(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val validCacheMap = CacheMap(cacheMapId, Map(ContractStartedPage.toString -> Json.toJson(Answers("",0))))
      when(mockDataCacheConnector.save(Matchers.any())).thenReturn(Future.successful(validCacheMap))
      val userAnswers: UserAnswers => Call = UserAnswers => Call("/POST","/foo")
      when(mockDecisionService.decide(Matchers.any(),Matchers.any(),Matchers.any())(Matchers.any(),Matchers.any(),Matchers.any()))
        .thenReturn(Future.successful(Result(ResponseHeader(SEE_OTHER),HttpEntity.Strict(ByteString(""),None))))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
