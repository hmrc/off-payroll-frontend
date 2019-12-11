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


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.WorkerUsingIntermediaryFormProvider
import models._
import models.requests.DataRequest
import models.sections.setup.WorkerType
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.WorkerUsingIntermediaryPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.WorkerUsingIntermediaryView

class WorkerUsingIntermediaryControllerSpec extends ControllerSpecBase {

  val formProvider = new WorkerUsingIntermediaryFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[WorkerUsingIntermediaryView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) =
    new WorkerUsingIntermediaryController(
      FakeIdentifierAction,
      dataRetrievalAction,
      new DataRequiredActionImpl(messagesControllerComponents),
      formProvider,
      messagesControllerComponents,
      view,
      compareAnswerService = mockCompareAnswerService,
      dataCacheConnector = mockDataCacheConnector,
      navigator = FakeSetupNavigator,
      frontendAppConfig
    )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WorkerUsingIntermediaryPage.toString -> Json.toJson(true))

  "WorkerType Controller" must {

    "return OK and the correct view for a GET in the optimised view" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered for the normal flow" in {


      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "return a Bad Request and errors when invalid data is submitted for the normal flow" in {


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
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
