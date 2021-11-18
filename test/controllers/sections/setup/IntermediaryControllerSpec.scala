/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package controllers.sections.setup

import controllers.ControllerSpecBase
import controllers.actions._
import navigation.mocks.FakeNavigators.FakeSetupNavigator
import play.api.test.Helpers._
import views.html.sections.setup.IntermediaryView

class IntermediaryControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[IntermediaryView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: UserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new IntermediaryController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType = requireUserType,
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    navigator = FakeSetupNavigator
  )

  def viewAsString = view(controllers.routes.StartAgainController.redirectToDisclaimer
)(fakeRequest, messages, frontendAppConfig).toString

  "IntermediaryController" must {

    "return OK and the correct view for a GET" in {

      val result = controller().onPageLoad(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to Index for a GET if no existing data is found" in {

      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad
.url)
    }



    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad()(fakeRequest)
      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong
.url)
    }
  }
}




