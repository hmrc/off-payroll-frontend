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

import config.SessionKeys
import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.{AboutYouFormProvider, WhichDescribesYouFormProvider}
import models._
import models.requests.DataRequest
import models.sections.setup.{AboutYouAnswer, WhichDescribesYouAnswer}
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.WhichDescribesYouPage
import play.api.data.Form
import play.api.libs.json._
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.SessionUtils._
import views.html.sections.setup.WhichDescribesYouView

class AboutYouControllerSpec extends ControllerSpecBase {

  val aboutYouFormProvider = new AboutYouFormProvider()
  val aboutYouForm = aboutYouFormProvider()
  val whichDescribesYouFormProvider = new WhichDescribesYouFormProvider()
  val whichDescribesYouForm = whichDescribesYouFormProvider()

  val whichDescribesYouview = injector.instanceOf[WhichDescribesYouView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new AboutYouController(
    appConfig = frontendAppConfig,
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    aboutYouFormProvider = new AboutYouFormProvider(),
    whichDescribesYouFormProvider = new WhichDescribesYouFormProvider(),
    controllerComponents = messagesControllerComponents,
    whichDescribesYouView = whichDescribesYouview,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,

    navigator = FakeSetupNavigator
  )

  "AboutYou Controller" when {

    "Optimised Feature Switch is enabled" must {

      def viewAsString(form: Form[_] = whichDescribesYouForm) = whichDescribesYouview(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

      val validData = Map(WhichDescribesYouPage.toString -> Json.toJson(WhichDescribesYouAnswer.values.head))

      "return OK and the correct view for a GET" in {

        val result = controller().onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val getRelevantData = new FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        contentAsString(result) mustBe viewAsString(whichDescribesYouForm.fill(WhichDescribesYouAnswer.values.head))
      }

      "redirect to the next page when valid data is submitted" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhichDescribesYouAnswer.values.head.toString))

        val answers = userAnswers.set(WhichDescribesYouPage,WhichDescribesYouAnswer.WorkerPAYE)
        mockConstructAnswers(DataRequest(postRequest,"id",answers),AboutYouAnswer)(answers)
        mockSave(CacheMap(cacheMapId, validData))(CacheMap(cacheMapId, validData))

        val result = controller().onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(onwardRoute.url)
        session(result).getModel[UserType](SessionKeys.userType) mustBe Some(UserType(WhichDescribesYouAnswer.values.head))
      }

      "return a Bad Request and errors when invalid data is submitted" in {

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
        val boundForm = whichDescribesYouForm.bind(Map("value" -> "invalid value"))

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

        val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "worker"))
        val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
      }
    }
  }
}
