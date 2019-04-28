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

package controllers

import play.api.data.Form
import play.api.libs.json.{JsArray, JsString, Json}
import uk.gov.hmrc.http.cache.client.CacheMap
import navigation.FakeNavigator
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.CannotClaimAsExpenseFormProvider
import models.CannotClaimAsExpense.WorkerProvidedMaterials
import models.{CannotClaimAsExpense, ErrorTemplate, NormalMode, UserAnswers}
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import pages.CannotClaimAsExpensePage
import play.api.mvc.Call
import play.api.mvc.Results.Redirect
import uk.gov.hmrc.http.HeaderCarrier
import views.html.CannotClaimAsExpenseView

import scala.concurrent.Future

class CannotClaimAsExpenseControllerSpec extends ControllerSpecBase {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new CannotClaimAsExpenseFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[CannotClaimAsExpenseView]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new CannotClaimAsExpenseController(
    dataCacheConnector = new FakeDataCacheConnector,
    navigator = new FakeNavigator(onwardRoute),
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    decisionService,
    appConfig = frontendAppConfig
  )

  def viewAsString(form: Form[_] = form) = view(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  "CannotClaimAsExpense Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(CannotClaimAsExpensePage.toString -> Json.toJson(Seq(CannotClaimAsExpense.values.head.toString)))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(Seq(CannotClaimAsExpense.values.head)))
    }

    "redirect to the next page when valid data is submitted" in {

      implicit val hc = new HeaderCarrier()

      val userAnswers = UserAnswers("id").set(CannotClaimAsExpensePage, 0,Seq(WorkerProvidedMaterials))

      when(decisionService.decide(Matchers.eq(userAnswers),Matchers.eq(onwardRoute),
        Matchers.eq(ErrorTemplate("cannotClaimAsExpense.title")))
      (any(),any(),any())).thenReturn(Future.successful(Redirect(onwardRoute)))


      val postRequest = fakeRequest.withFormUrlEncodedBody(("cannotClaimAsExpense[0]", CannotClaimAsExpense.options.head.value))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("cannotClaimAsExpense[0]", "invalid value"))
      val boundForm = form.bind(Map("cannotClaimAsExpense[0]" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("cannotClaimAsExpense[0]", CannotClaimAsExpense.options.head.value))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
