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

import config.featureSwitch.FeatureSwitching
import connectors.mocks.MockDataCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.control.MoveWorkerFormProvider

import models.requests.DataRequest
import models.sections.control.MoveWorker
import models.NormalMode
import navigation.mocks.FakeNavigators.FakeControlNavigator
import pages.sections.control.MoveWorkerPage
import play.api.data.Form
import play.api.libs.json._
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.control.MoveWorkerView

class MoveWorkerControllerSpec extends ControllerSpecBase with MockDataCacheConnector with FeatureSwitching {

  val formProvider = new MoveWorkerFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val optimisedView = injector.instanceOf[MoveWorkerView]

  def controller(dataRetrievalAction: DataRetrievalAction =
                 FakeEmptyCacheMapDataRetrievalAction) = new MoveWorkerController(
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    optimisedView = optimisedView,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,

    navigator = FakeControlNavigator
  )

  def optimisedViewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(MoveWorkerPage.toString -> Json.toJson(MoveWorker.CanMoveWorkerWithPermission))

  "MoveWorker Controller" must {

    "return OK and the correct view for a GET for optimised view" in {


      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe optimisedViewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered for optimised view" in {


      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe optimisedViewAsString(form.fill(MoveWorker.CanMoveWorkerWithPermission))
    }

    "redirect to the next page when valid data is submitted for optimised view" in {


      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", MoveWorker.CanMoveWorkerWithPermission.toString))

      val answers = userAnswers.set(MoveWorkerPage,MoveWorker.CanMoveWorkerWithPermission)
      mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),MoveWorker)(answers)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted for optimised view" in {


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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", MoveWorker.options.head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
