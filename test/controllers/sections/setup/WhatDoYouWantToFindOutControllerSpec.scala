/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import controllers.ControllerSpecBase
import controllers.actions._
import forms.sections.setup.WhatDoYouWantToFindOutFormProvider
import models._
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToFindOut
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import pages.sections.setup.WhatDoYouWantToFindOutPage
import play.api.data.Form
import play.api.libs.json.Json
import play.api.test.Helpers._
import uk.gov.hmrc.http.cache.client.CacheMap
import views.html.sections.setup.WhatDoYouWantToFindOutView

class WhatDoYouWantToFindOutControllerSpec extends ControllerSpecBase {

  val formProvider = new WhatDoYouWantToFindOutFormProvider()
  val form = formProvider()

  val view = injector.instanceOf[WhatDoYouWantToFindOutView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) =
    new WhatDoYouWantToFindOutController(
      identify = FakeIdentifierAction,
      getData = dataRetrievalAction,
      requireData = new DataRequiredActionImpl(messagesControllerComponents),
      whatDoYouWantToFindOutFormProvider = formProvider,
      controllerComponents = messagesControllerComponents,
      whatDoYouWantToFindOutView = view,
      checkYourAnswersService = mockCheckYourAnswersService,
      compareAnswerService = mockCompareAnswerService,
      dataCacheConnector = mockDataCacheConnector,
      navigator = FakeSetupNavigator,
      appConfig = frontendAppConfig
    )

  def viewAsString(form: Form[_] = form) = view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

  val validData = Map(WhatDoYouWantToFindOutPage.toString -> Json.toJson(WhatDoYouWantToFindOut.values.head))

  "WhatDoYouWantToFindOut Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val getRelevantData = FakeGeneralDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(WhatDoYouWantToFindOut.values.head))
    }

    "redirect to the next page when valid data is submitted" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhatDoYouWantToFindOut.options.head.value))
      val answers = userAnswers.set(WhatDoYouWantToFindOutPage,WhatDoYouWantToFindOut.IR35)
      mockConstructAnswers(DataRequest(postRequest,"id",answers),WhatDoYouWantToFindOut)(answers)

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
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhatDoYouWantToFindOut.options.head.value))
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
