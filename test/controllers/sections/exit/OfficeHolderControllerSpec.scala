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

package controllers.sections.exit


import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.exit.OfficeHolderFormProvider
import models.requests.DataRequest
import models.{Answers, CheckMode, NormalMode}
import navigation.mocks.FakeNavigators.FakeExitNavigator
import pages.sections.exit.OfficeHolderPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.exit.OfficeHolderView

class OfficeHolderControllerSpec extends ControllerSpecBase {

  val formProvider = new OfficeHolderFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val optimisedView = injector.instanceOf[OfficeHolderView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new OfficeHolderController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig,
    optimisedView = optimisedView,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,

    navigator = FakeExitNavigator
  )

  val validData = Map(OfficeHolderPage.toString -> Json.toJson(Answers(true, 0)))

  "OfficeHolder Controller" must {

    "override the mode if office holder set to false in check mode" in {

      val answers = userAnswers.set(OfficeHolderPage, 0, false)

      val validData = Map(OfficeHolderPage.toString -> Json.toJson(Answers(false, 0)))

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

      val result = controller(getRelevantData).onSubmit(CheckMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "If the OptimisedFlow is enabled" should {

      def viewAsString(form: Form[_] = form) = optimisedView(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      "return OK and the correct view for a GET" in {


        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {


        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))
        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(form.fill(true))
      }

      "redirect to the next page when valid data is submitted" in {

        val answers = userAnswers.set(OfficeHolderPage, 0, true)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
      }

      "override the mode if changing from Yes to No, so that Normal flow is continued" in {

        val answers = userAnswers.set(OfficeHolderPage, 0, true)

        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

        mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

        val result = controller().onSubmit(CheckMode)(postRequest)

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
        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
