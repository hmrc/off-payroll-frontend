/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.WhoAreYouFormProvider
import models._
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.sections.setup.WhoAreYou
import models.sections.setup.WhoAreYou.{Worker, _}
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.{WhatDoYouWantToFindOutPage, WhoAreYouPage}
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.WhoAreYouView

class WhoAreYouControllerSpec extends ControllerSpecBase {

  val formProvider = new WhoAreYouFormProvider()
  val form = formProvider()(fakeDataRequest)

  val view = injector.instanceOf[WhoAreYouView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) =
    new WhoAreYouController(
      identify = FakeIdentifierAction,
      getData = dataRetrievalAction,
      requireData = new DataRequiredActionImpl(messagesControllerComponents),
      whoAreYouFormProvider = formProvider,
      controllerComponents = messagesControllerComponents,
      view = view,
      compareAnswerService = mockCompareAnswerService,
      dataCacheConnector = mockDataCacheConnector,
      navigator = FakeSetupNavigator,
      appConfig = frontendAppConfig
    )

  def viewAsString(form: Form[_] = form, showAgency: Boolean = false) =
    view(
      controllers.sections.setup.routes.WhoAreYouController.onSubmit(NormalMode),
      form,
      NormalMode,
      showAgency
    )(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WhoAreYouPage.toString -> Json.toJson[WhoAreYou](Worker))

  "WhoAreYou Controller" must {

    "If the user answered 'IR35' as the reason for visit" should {

      "return OK and the correct view for a GET which includes the Agency radio option" in {

        val userAnswers = UserAnswers("id").set(WhatDoYouWantToFindOutPage, IR35)

        val getRelevantData = FakeGeneralDataRetrievalAction(Some(userAnswers.cacheMap))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString(showAgency = true)
      }
    }

    "If the user answered 'PAYE' as the reason for visit" should {

      "return OK and the correct view for a GET which DOES NOT include the Agency radio option" in {

        val userAnswers = UserAnswers("id").set(WhatDoYouWantToFindOutPage, PAYE)

        val getRelevantData = FakeGeneralDataRetrievalAction(Some(userAnswers.cacheMap))

        val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustBe OK
        contentAsString(result) mustBe viewAsString()
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(Worker))
    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", Worker.toString))
      val answers = userAnswers.set(WhoAreYouPage, Worker)
      mockConstructAnswers(DataRequest(postRequest,"id",answers), Worker)(answers)
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

    "redirect to Index Controller for a GET if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }

    "redirect to Index Controller for a POST if no existing data is found" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", Worker.toString))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }
  }
}
