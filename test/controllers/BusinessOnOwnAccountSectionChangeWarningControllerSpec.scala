/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

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

import controllers.actions._
import controllers.sections.businessOnOwnAccount.{routes => businessOnOwnAccountRoutes}
import handlers.ErrorHandler
import models.CheckMode
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.exit.OfficeHolderPage
import play.api.test.Helpers._
import views.html.BusinessOnOwnAccountSectionChangeWarningView

class BusinessOnOwnAccountSectionChangeWarningControllerSpec extends ControllerSpecBase {

  val view = injector.instanceOf[BusinessOnOwnAccountSectionChangeWarningView]

  def controller(dataRetrievalAction: DataRetrievalAction = FakeEmptyCacheMapDataRetrievalAction,
                 requireUserType: FakeUserTypeRequiredAction = FakeUserTypeRequiredSuccessAction) = new BusinessOnOwnAccountSectionChangeWarningController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    requireData = new DataRequiredActionImpl(messagesControllerComponents),
    requireUserType,
    controllerComponents = messagesControllerComponents,
    view = view,
    appConfig = frontendAppConfig,
    checkYourAnswersService = mockCheckYourAnswersService,
    dataCacheConnector = mockDataCacheConnector,
    errorHandler = app.injector.instanceOf[ErrorHandler]
  )

  val submitAction = routes.BusinessOnOwnAccountSectionChangeWarningController.onSubmit(ExtendContractPage)

  def viewAsString = view(submitAction)(fakeRequest, messages, frontendAppConfig).toString

  "AgencyAdvisory Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(ExtendContractPage)(fakeRequest)
      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString
    }

    "redirect to the ExtendContract page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(ExtendContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.ExtendContractController.onPageLoad(CheckMode).url)
    }

    "redirect to the FirstContract page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(FirstContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.FirstContractController.onPageLoad(CheckMode).url)
    }

    "redirect to the FollowOnContract page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(FollowOnContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.FollowOnContractController.onPageLoad(CheckMode).url)
    }

    "redirect to the MajorityOfWorkingTime page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(MajorityOfWorkingTimePage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.MajorityOfWorkingTimeController.onPageLoad(CheckMode).url)
    }

    "redirect to the MultipleContracts page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(MultipleContractsPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.MultipleContractsController.onPageLoad(CheckMode).url)
    }

    "redirect to the OwnershipRights page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(OwnershipRightsPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.OwnershipRightsController.onPageLoad(CheckMode).url)
    }

    "redirect to the PermissionToWorkWithOthers page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(PermissionToWorkWithOthersPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.PermissionToWorkWithOthersController.onPageLoad(CheckMode).url)
    }

    "redirect to the PreviousContract page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(PreviousContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.PreviousContractController.onPageLoad(CheckMode).url)
    }

    "redirect to the RightsOfWork page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(RightsOfWorkPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.RightsOfWorkController.onPageLoad(CheckMode).url)
    }

    "redirect to the SimilarWorkOtherClients page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(SimilarWorkOtherClientsPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.SimilarWorkOtherClientsController.onPageLoad(CheckMode).url)
    }

    "redirect to the TransferOfRights page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(TransferOfRightsPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.TransferOfRightsController.onPageLoad(CheckMode).url)
    }

    "redirect to the WorkerKnown page if that was the selected page to change" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(WorkerKnownPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(businessOnOwnAccountRoutes.WorkerKnownController.onPageLoad(CheckMode).url)
    }

    "redirect to the something went wrong page when no user type is given" in {

      val result = controller(requireUserType = FakeUserTypeRequiredFailureAction).onPageLoad(ExtendContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.StartAgainController.somethingWentWrong().url)
    }

    "render an ISE if the page is invalid" in {

      val answers = userAnswers.set(BusinessOnOwnAccountSectionChangeWarningPage, true)
      mockSave(answers.cacheMap)(answers.cacheMap)

      val result = controller().onSubmit(OfficeHolderPage)(fakeRequest)
      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to Index Controller for a GET if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onPageLoad(ExtendContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }

    "redirect to Index Controller for a POST if no existing data is found" in {
      val result = controller(FakeDontGetDataDataRetrievalAction).onSubmit(ExtendContractPage)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}




