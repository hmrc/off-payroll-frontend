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

import config.SessionKeys
import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.AboutYouFormProvider
import models.{AboutYouAnswer, Answers, NormalMode, UserType}
import navigation.FakeNavigator
import pages.AboutYouPage
import play.api.data.Form
import play.api.libs.json._
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.AboutYouView
import models.Answers._
import utils.SessionUtils._

class AboutYouControllerSpec extends ControllerSpecBase {

  def onwardRoute = Call("GET", "/foo")

  val formProvider = new AboutYouFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[AboutYouView]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new AboutYouController(
    appConfig = frontendAppConfig,
    dataCacheConnector = new FakeDataCacheConnector,
    navigator = new FakeNavigator(onwardRoute),
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  def viewAsString(form: Form[_] = form) = view(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  "AboutYou Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(AboutYouPage.toString -> Json.toJson(Answers(AboutYouAnswer.values.head,0)))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AboutYouAnswer.values.head))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", AboutYouAnswer.values.head.toString))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
      session(result).getModel[UserType](SessionKeys.userType) mustBe Some(UserType(AboutYouAnswer.values.head))
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "worker"))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
