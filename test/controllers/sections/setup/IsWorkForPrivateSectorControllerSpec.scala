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

import config.featureSwitch.OptimisedFlow
import controllers.ControllerSpecBase
import controllers.actions._
import forms.IsWorkForPrivateSectorFormProvider
import models._
import models.requests.DataRequest
import pages.sections.setup.{ContractStartedPage, IsWorkForPrivateSectorPage}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.IsWorkForPrivateSectorView

class IsWorkForPrivateSectorControllerSpec extends ControllerSpecBase {

  override def beforeEach(): Unit = {
    super.beforeEach()
    enable(OptimisedFlow)
  }

  val formProvider = new IsWorkForPrivateSectorFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[IsWorkForPrivateSectorView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new IsWorkForPrivateSectorController(
    appConfig = frontendAppConfig,
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    decisionService = mockDecisionService,
    navigator = FakeSetupNavigator
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  "IsWorkForPrivateSector Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(IsWorkForPrivateSectorPage.toString -> Json.toJson(Answers(true,0)))
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val validData = Map(ContractStartedPage.toString -> Json.toJson(Answers(true,0)))

      val answers = userAnswers.set(ContractStartedPage,0,true)
      mockOptimisedConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

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

    "redirect to Index for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
