/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package controllers

import controllers.actions._
import controllers.sections.personalService.{routes => personalServiceRoutes}
import handlers.ErrorHandler
import models.CheckMode
import pages.PersonalServiceSectionChangeWarningPage
import pages.sections.exit.OfficeHolderPage
import pages.sections.personalService._
import play.api.test.Helpers._
import views.html.PersonalServiceSectionChangeWarningView

class PersonalServiceSectionChangeWarningControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[PersonalServiceSectionChangeWarningView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction) = new PersonalServiceSectionChangeWarningController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    dataCacheConnector = mockDataCacheConnector,
    errorHandler = app.injector.instanceOf[ErrorHandler]
  )

  val submitAction = routes.PersonalServiceSectionChangeWarningController.onSubmit(ArrangedSubstitutePage)

  def viewAsString = view(submitAction)(fakeRequest, messages, frontendAppConfig).toString

  "AgencyAdvisory Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(ArrangedSubstitutePage)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to the ArrangedSubstitute page if that was the selected page to change" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(ArrangedSubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(personalServiceRoutes.ArrangedSubstituteController.onPageLoad(CheckMode).url)
    }

    "redirect to the RejectSubstitute page if that was the selected page to change" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(RejectSubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(personalServiceRoutes.RejectSubstituteController.onPageLoad(CheckMode).url)
    }

    "redirect to the DidPaySubstitute page if that was the selected page to change" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(DidPaySubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(personalServiceRoutes.DidPaySubstituteController.onPageLoad(CheckMode).url)
    }

    "redirect to the NeededToPayHelper page if that was the selected page to change" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(NeededToPayHelperPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(personalServiceRoutes.NeededToPayHelperController.onPageLoad(CheckMode).url)
    }

    "redirect to the WouldWorkerPaySubstitute page if that was the selected page to change" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(WouldWorkerPaySubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url)
    }

    "render an ISE if the page is invalid" in {

      val answers = userAnswers.set(PersonalServiceSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(OfficeHolderPage)(fakeRequest)
      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(ArrangedSubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(ArrangedSubstitutePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad.url)

    }
  }
}




