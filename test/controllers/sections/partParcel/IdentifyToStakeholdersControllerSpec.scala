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

import connectors.FakeDataCacheConnector
import connectors.mocks.MockMongoCacheConnector
import controllers.ControllerSpecBase
import controllers.actions._
import forms.IdentifyToStakeholdersFormProvider
import models.Answers._
import models.IdentifyToStakeholders.WorkForEndClient
import models._
import navigation.FakeNavigator
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import pages.sections.partParcel.IdentifyToStakeholdersPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.Call
import play.api.mvc.Results.Redirect
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.subOptimised.sections.partParcel.IdentifyToStakeholdersView

import scala.concurrent.Future

class IdentifyToStakeholdersControllerSpec extends ControllerSpecBase with MockMongoCacheConnector {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new IdentifyToStakeholdersFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[IdentifyToStakeholdersView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new IdentifyToStakeholdersController(
    mockMongoCacheConnector,
    new FakeNavigator(onwardRoute),
    FakeIdentifierAction,
    dataRetrievalAction,
    new DataRequiredActionImpl(messagesControllerComponents),
    formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    decisionService,
    frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(IdentifyToStakeholdersPage.toString -> Json.toJson(Answers(IdentifyToStakeholders.values.head,0)))

  "IdentifyToStakeholders Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(IdentifyToStakeholders.values.head))
    }

    "redirect to the next page when valid data is submitted" in {

      implicit val hc = new HeaderCarrier()

      val userAnswers = UserAnswers("id").set(IdentifyToStakeholdersPage,0, WorkForEndClient)

      mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))
      when(decisionService.decide(Matchers.eq(userAnswers),Matchers.eq(onwardRoute),
        Matchers.eq(ErrorTemplate("identifyToStakeholders.title")))
      (any(),any(),any())).thenReturn(Future.successful(Redirect(onwardRoute)))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", IdentifyToStakeholders.options.head.value))

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
