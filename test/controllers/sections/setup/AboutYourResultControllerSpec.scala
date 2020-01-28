/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import controllers.ControllerSpecBase
import controllers.actions._
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import play.api.test.Helpers._
import views.html.sections.setup.AboutYourResultView

class AboutYourResultControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[AboutYourResultView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new AboutYourResultController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeSetupNavigator
  )

  def viewAsString = view(routes.AboutYourResultController.onSubmit())(fakeRequest, messages, frontendAppConfig).toString

  "AboutYou Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to the next page when valid data is submitted" in {
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
