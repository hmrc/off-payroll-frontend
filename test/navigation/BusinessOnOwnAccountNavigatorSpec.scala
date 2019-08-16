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
import controllers.sections.businessOnOwnAccount.{routes => booaRoutes}
import controllers.routes
import models._
import pages._
import pages.sections.businessOnOwnAccount.{ExtendContractPage, FinanciallyDependentPage, FirstContractPage, OwnershipRightsPage, WorkerKnownPage}
import pages.sections.setup.ContractStartedPage

class BusinessOnOwnAccountNavigatorSpec extends GuiceAppSpecBase {

  val emptyUserAnswers = UserAnswers("id")
  val navigator = new BusinessOnOwnAccountNavigator

  def nextPage(fromPage: Page, userAnswers: UserAnswers = emptyUserAnswers) = navigator.nextPage(fromPage, NormalMode)(userAnswers)

  "BusinessOnOwnAccountNavigator" must {

    "go from the WorkerKnownPage to the MultipleContractsPage" in {
      nextPage(WorkerKnownPage) mustBe booaRoutes.MultipleContractsController.onPageLoad(NormalMode)
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
          .set(WorkerKnownPage, true)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.RightsOfWorkController.onPageLoad(NormalMode)
      }

      "the WorkerKnown answer is true should go to the PreviousContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(OwnershipRightsPage, false)
          .set(WorkerKnownPage, true)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }

      "the ContractStarted answer is true should go to the PreviousContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(OwnershipRightsPage, false)
          .set(ContractStartedPage, true)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }

      "the OwnershipRights and WorkerKnown answers are both false should go to the FirstContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(OwnershipRightsPage, false)
          .set(WorkerKnownPage, false)

        nextPage(OwnershipRightsPage, userAnswers) mustBe booaRoutes.FirstContractController.onPageLoad(NormalMode)
      }
    }

    "go from the RightsOfWorkPage" when {

      "the RightsOfWork answer is false should go to the TransferOfRightsPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, false)
          .set(WorkerKnownPage, true)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.TransferOfRightsController.onPageLoad(NormalMode)
      }

      "the WorkerKnown answer is true should go to the PreviousContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, true)
          .set(WorkerKnownPage, true)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }

      "the ContractStarted answer is true should go to the PreviousContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, true)
          .set(ContractStartedPage, true)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.PreviousContractController.onPageLoad(NormalMode)
      }

      "the RightsOfWork answer is true and WorkerKnown answer is false should go to the FirstContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(RightsOfWorkPage, true)
          .set(WorkerKnownPage, false)

        nextPage(RightsOfWorkPage, userAnswers) mustBe booaRoutes.FirstContractController.onPageLoad(NormalMode)
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

      "the FollowOnContract answer is false should go to the ExtendContractPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(FollowOnContractPage, false)

        nextPage(FollowOnContractPage, userAnswers) mustBe booaRoutes.ExtendContractController.onPageLoad(NormalMode)
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

    "go from the FinanciallyDependentPage" when {

      "the WorkerKnown answer is true should go to the SimilarWorkOtherClientsPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(WorkerKnownPage, true)

        nextPage(FinanciallyDependentPage, userAnswers) mustBe booaRoutes.SimilarWorkOtherClientsController.onPageLoad(NormalMode)
      }

      "the ContractStarted answer is true should go to the SimilarWorkOtherClientsPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(ContractStartedPage, true)

        nextPage(FinanciallyDependentPage, userAnswers) mustBe booaRoutes.SimilarWorkOtherClientsController.onPageLoad(NormalMode)
      }

      "the Worker is not known should go to the CheckYourAnswersPage" in {

        enable(OptimisedFlow)
        lazy val userAnswers = UserAnswers("id")
          .set(WorkerKnownPage, false)
          .set(ContractStartedPage, false)

        nextPage(FinanciallyDependentPage, userAnswers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }

    "go from the SimilarWorkOtherClientsPage to the CheckYourAnswersPage" in {
      nextPage(SimilarWorkOtherClientsPage) mustBe routes.CheckYourAnswersController.onPageLoad()
    }
  }
}