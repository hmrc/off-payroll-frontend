/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import assets.messages.CheckYourAnswersMessages
import controllers.actions._
import models._
import navigation.mocks.FakeNavigators.FakeCYANavigator
import pages.sections.exit.OfficeHolderPage
import play.api.test.Helpers._
import services.CheckYourAnswersService
import services.mocks.MockCheckYourAnswersValidationService
import views.html.CheckYourAnswersView

class CheckYourAnswersControllerSpec extends ControllerSpecBase with MockCheckYourAnswersValidationService {

  val view = injector.instanceOf[CheckYourAnswersView]
  val mockCheckAnswerService = app.injector.instanceOf[CheckYourAnswersService]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: FakeUserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new CheckYourAnswersController(
    navigator = FakeCYANavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType = requireUserType,
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    compareAnswerService = mockCompareAnswerService,
    dataCacheConnector = mockDataCacheConnector,
    errorHandler = errorHandler,
    checkYourAnswersValidationService = mockCheckYourAnswersValidationService
  )

  "CheckYourAnswers Controller" must {

    "If the response from the CheckYourAnswersValidationService is valid" should {

      "return OK and the correct view for a GET" in {

        mockCheckYourAnswers(Seq.empty)
        mockIsValid(UserAnswers(cacheMapId))(Right(true))

        val result = controller().onPageLoad()(fakeRequest)
        status(result) mustBe OK
        titleOf(result) mustBe title(CheckYourAnswersMessages.title)
      }
    }

    "If the response from the CheckYourAnswersValidationService is invalid (set of unanswered questions)" should {

      "return Redirect (303)" in {

        mockIsValid(UserAnswers(cacheMapId))(Left(Set(OfficeHolderPage)))

        val result = controller().onPageLoad()(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong.url)

      }
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad()(fakeRequest)

      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong.url)

    }


    "redirect to the result page" in {
      val result = controller().onSubmit(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }
  }
}




