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

package navigation

import base.GuiceAppSpecBase
import config.featureSwitch.OptimisedFlow
import controllers.routes
import controllers.sections.businessOnOwnAccount.{routes => booaRoutes}
import models._
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.setup.WhoAreYouPage

class BusinessOnOwnAccountNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new BusinessOnOwnAccountNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers, mode: Mode = NormalMode) = navigator.nextPage(fromPage, mode)(userAnswers)

  "BusinessOnOwnAccountNavigator" must {

    "for the start page" should {

      "redirect to the MultipleContracts page if user is Worker" in {
        navigator.startPage(emptyUserAnswers.set(WhoAreYouPage, WhoAreYou.Worker)) mustBe booaRoutes.MultipleContractsController.onPageLoad(NormalMode)
      }

      "redirect to the WorkerKnown page if user is Hirer" in {
        navigator.startPage(emptyUserAnswers.set(WhoAreYouPage, WhoAreYou.Client)) mustBe booaRoutes.WorkerKnownController.onPageLoad(NormalMode)
      }
    }

    "go from the WorkerKnownPage to the WorkerKnownPage if workerKnown is not answered somehow" in {
      nextPage(WorkerKnownPage) mustBe booaRoutes.WorkerKnownController.onPageLoad(NormalMode)
    }

    "go from the WorkerKnownPage to the MultipleContractsPage if workerKnown is answered Yes" in {
      nextPage(WorkerKnownPage,userAnswers = emptyUserAnswers.set(WorkerKnownPage,true)) mustBe booaRoutes.MultipleContractsController.onPageLoad(NormalMode)
    }

    "go from the WorkerKnownPage to the CYA page if workerKnown is answered No" in {
      nextPage(WorkerKnownPage,userAnswers = emptyUserAnswers.set(WorkerKnownPage,false)) mustBe routes.CheckYourAnswersController.onPageLoad(None)
    }

    "go from the MultipleContractsPage" when {

      "the MultipleContracts answer is false should go to the PermissionToWorkWithOthersPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(MultipleContractsPage, false)

        nextPage(MultipleContractsPage, userAnswers) mustBe booaRoutes.PermissionToWorkWithOthersController.onPageLoad(NormalMode)
      }

      "the MultipleContracts answer is true should go to the OwnershipRightsPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(MultipleContractsPage, true)

        nextPage(MultipleContractsPage, userAnswers) mustBe booaRoutes.OwnershipRightsController.onPageLoad(NormalMode)
      }
    }

    "go from the PermissionToWorkWithOthersPage to the OwnershipRightsPage" in {
      nextPage(PermissionToWorkWithOthersPage) mustBe booaRoutes.OwnershipRightsController.onPageLoad(NormalMode)
    }

    "go from the OwnershipRightsPage" when {

      "the OwnershipRights answer is true should go to the RightsOfWorkPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(OwnershipRightsPage, true)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.RightsOfWorkController.onPageLoad(NormalMode)
      }

      "the OwnershipRights answer is false should go to other PreviousContractController" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(OwnershipRightsPage, false)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }
    }

    "go from the RightsOfWorkPage" when {

      "the RightsOfWork answer is false should go to the TransferOfRightsPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, false)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.TransferOfRightsController.onPageLoad(NormalMode)
      }

      "the RightsOfWork answer is true should go to the PreviousContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, true)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }
    }

    "go from the TransferOfRightsPage to the PreviousContractPage" in {
      nextPage(TransferOfRightsPage) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
    }

    "go from the PreviousContractPage" when {

      "the PreviousContract answer is true should go to the FollowOnContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(PreviousContractPage, true)

        nextPage(PreviousContractPage, userAnswers) mustBe booaRoutes.FollowOnContractController.onPageLoad(NormalMode)
      }

      "the PreviousContract answer is false should go to the FirstContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(PreviousContractPage, false)

        nextPage(PreviousContractPage, userAnswers) mustBe booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }
    }

    "go from the FollowOnContractPage" when {

      "the FollowOnContract answer is false should go to the FirstContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(FollowOnContractPage, false)

        nextPage(FollowOnContractPage, userAnswers) mustBe booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }

      "the FollowOnContract answer is true should go to the MajorityOfWorkingTimePage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(FollowOnContractPage, true)

        nextPage(FollowOnContractPage, userAnswers) mustBe booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)
      }
    }

    "go from the FirstContractPage" when {

      "the FirstContract answer is false should go to the ExtendContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(FirstContractPage, false)

        nextPage(FirstContractPage, userAnswers) mustBe booaRoutes.ExtendContractController.onPageLoad(NormalMode)
      }

      "the FirstContract answer is true should go to the MajorityOfWorkingTimePage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(FirstContractPage, true)

        nextPage(FirstContractPage, userAnswers) mustBe booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)
      }
    }

    "go from the ExtendContractPage to the MajorityOfWorkingTimePage" in {
      nextPage(ExtendContractPage) mustBe booaRoutes.MajorityOfWorkingTimeController.onPageLoad(NormalMode)
    }

    "go from the MajorityOfWorkingTimePage to the FinanciallyDependentPage" in {
      nextPage(MajorityOfWorkingTimePage) mustBe booaRoutes.FinanciallyDependentController.onPageLoad(NormalMode)
    }

    "go from the FinanciallyDependentPage to the SimilarWorkOtherClientsPage" in {
      nextPage(FinanciallyDependentPage) mustBe booaRoutes.SimilarWorkOtherClientsController.onPageLoad(NormalMode)
    }

    "go from the SimilarWorkOtherClientsPage" when {

      "in normal mode to the CheckYourAnswersPage" in {
        nextPage(SimilarWorkOtherClientsPage, mode = NormalMode) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "in check mode to the CheckYourAnswersPage" in {
        nextPage(SimilarWorkOtherClientsPage, mode = CheckMode) mustBe routes.CheckYourAnswersController.onPageLoad(Some(Section.businessOnOwnAccount))
      }
    }
  }
}