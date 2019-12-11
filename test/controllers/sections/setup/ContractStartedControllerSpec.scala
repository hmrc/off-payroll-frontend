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


import connectors.mocks.MockAuditConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.ContractStartedFormProvider
import models.{AuditJourneyStart, AuditResult, NormalMode}
import models.requests.DataRequest
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.ContractStartedPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap

class ContractStartedControllerSpec extends ControllerSpecBase with MockAuditConnector {

  val formProvider = new ContractStartedFormProvider()
  val form = formProvider()(fakeDataRequest, frontendAppConfig)

  val view = injector.instanceOf[views.html.sections.setup.ContractStartedView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new ContractStartedController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeSetupNavigator,
    auditConnector = mockAuditConnector,
    appConfig = frontendAppConfig
  )

  def viewAsStringOptimised(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(ContractStartedPage.toString -> Json.toJson(true))

  "ContractStarted Controller" must {

    "return OK and the correct view for a GET for the normal flow" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsStringOptimised()
    }

    "populate the view correctly on a GET when the question has previously been answered for normal flow" in {

      val validData = Map(ContractStartedPage.toString -> Json.toJson(true))
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsStringOptimised(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val answers = userAnswers.set(ContractStartedPage,true)

      mockConstructAnswers(DataRequest(postRequest,"id",answers),Boolean)(answers)
      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
      mockAuditEvent("cestJourneyStart", AuditJourneyStart(userAnswers))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
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
