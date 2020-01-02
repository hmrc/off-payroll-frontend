/*
 * Copyright 2020 HM Revenue & Customs
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

import assets.messages._
import base.GuiceAppSpecBase
import play.api.i18n.Messages

class ErrorMessagesSpec extends GuiceAppSpecBase {

  "Messages file" should {

    "for the Setup Section" should {

      "have the correct error messages for the WantToFindOutPage" in {
        Messages("whatDoYouWantToFindOut.error.required") mustBe WhatDoYouWantToFindOutMessages.error
      }

      "have the correct error messages for the WhatDoYouWantToDoPage" in {
        Messages("whatDoYouWantToDo.error.required") mustBe WhatDoYouWantToDoMessages.error
      }

      "have the correct error messages for the WhoAreYouPage" in {
        Messages("whoAreYou.ir35.error.required") mustBe WhoAreYouMessages.ir35Error
        Messages("whoAreYou.paye.error.required") mustBe WhoAreYouMessages.payeError
      }

      "have the correct error messages for the WorkingUsingIntermediaryPage" in {
        Messages("worker.workerUsingIntermediary.error.required") mustBe WorkerUsingIntermediaryMessages.Worker.error
        Messages("hirer.workerUsingIntermediary.error.required") mustBe WorkerUsingIntermediaryMessages.Hirer.error
      }

      "have the correct error messages for the ContractStartedPage" in {
        Messages("worker.contractStarted.error.required") mustBe ContractStartedOptimisedMessages.Worker.error
        Messages("hirer.contractStarted.error.required") mustBe ContractStartedOptimisedMessages.Hirer.error
      }
    }


    "for the Early Exit Section" should {

      "have the correct error messages for the OfficeHolderPage" in {
        Messages("worker.officeHolder.error.required") mustBe OfficeHolderMessages.Worker.error
        Messages("hirer.officeHolder.error.required") mustBe OfficeHolderMessages.Hirer.error
      }
    }


    "for the Personal Service Section" should {

      "have the correct error messages for the ArrangedSubstitutePage" in {
        Messages("worker.arrangedSubstitute.error.required") mustBe ArrangedSubstituteMessages.Worker.error
        Messages("hirer.arrangedSubstitute.error.required") mustBe ArrangedSubstituteMessages.Hirer.error
      }

      "have the correct error messages for the RejectSubstitutePage" in {
        Messages("worker.rejectSubstitute.error.required") mustBe RejectSubstituteMessages.Worker.error
        Messages("hirer.rejectSubstitute.error.required") mustBe RejectSubstituteMessages.Hirer.error
      }

      "have the correct error messages for the DidPaySubstitutePage" in {
        Messages("worker.didPaySubstitute.error.required") mustBe DidPaySubstituteMessages.Worker.error
        Messages("hirer.didPaySubstitute.error.required") mustBe DidPaySubstituteMessages.Hirer.error
      }

      "have the correct error messages for the WouldWorkerPaySubstitutePage" in {
        Messages("worker.wouldWorkerPaySubstitute.error.required") mustBe WouldPaySubstituteMessages.Worker.error
        Messages("hirer.wouldWorkerPaySubstitute.error.required") mustBe WouldPaySubstituteMessages.Hirer.error
      }

      "have the correct error messages for the NeededToPayHelperPage" in {
        Messages("worker.neededToPayHelper.error.required") mustBe NeededToPayHelperMessages.Worker.error
        Messages("hirer.neededToPayHelper.error.required") mustBe NeededToPayHelperMessages.Hirer.error
      }
    }


    "for the Control Section" should {

      "have the correct error messages for the MoveWorkerPage" in {
        Messages("worker.moveWorker.error.required") mustBe MoveWorkerMessages.OptimisedWorker.error
        Messages("hirer.moveWorker.error.required") mustBe MoveWorkerMessages.OptimisedHirer.error
      }

      "have the correct error messages for the HowWorkIsDonePage" in {
        Messages("worker.howWorkIsDone.error.required") mustBe HowWorkIsDoneMessages.OptimisedWorker.error
        Messages("hirer.howWorkIsDone.error.required") mustBe HowWorkIsDoneMessages.OptimisedHirer.error
      }

      "have the correct error messages for the ScheduleOfWorkingHoursPage" in {
        Messages("worker.scheduleOfWorkingHours.error.required") mustBe ScheduleOfWorkingHoursMessages.OptimisedWorker.error
        Messages("hirer.scheduleOfWorkingHours.error.required") mustBe ScheduleOfWorkingHoursMessages.OptimisedHirer.error
      }

      "have the correct error messages for the ChooseWhereWorkPage" in {
        Messages("worker.chooseWhereWork.error.required") mustBe ChooseWhereWorkMessages.OptimisedWorker.error
        Messages("hirer.chooseWhereWork.error.required") mustBe ChooseWhereWorkMessages.OptimisedHirer.error
      }
    }


    "for the Financial Risk Section" should {

      "have the correct error messages for the EquipmentExpensesPage" in {
        Messages("worker.equipmentExpenses.error.required") mustBe EquipmentExpensesMessages.Worker.error
        Messages("hirer.equipmentExpenses.error.required") mustBe EquipmentExpensesMessages.Hirer.error
      }

      "have the correct error messages for the VehiclePage" in {
        Messages("worker.vehicle.error.required") mustBe VehicleMessages.Worker.error
        Messages("hirer.vehicle.error.required") mustBe VehicleMessages.Hirer.error
      }

      "have the correct error messages for the MaterialsPage" in {
        Messages("worker.materials.error.required") mustBe MaterialsMessages.Worker.error
        Messages("hirer.materials.error.required") mustBe MaterialsMessages.Hirer.error
      }

      "have the correct error messages for the OtherExpensesPage" in {
        Messages("worker.otherExpenses.error.required") mustBe OtherExpensesMessages.Worker.error
        Messages("hirer.otherExpenses.error.required") mustBe OtherExpensesMessages.Hirer.error
      }

      "have the correct error messages for the HowWorkerIsPaidPage" in {
        Messages("worker.howWorkerIsPaid.error.required") mustBe HowWorkerIsPaidMessages.WorkerOptimised.error
        Messages("hirer.howWorkerIsPaid.error.required") mustBe HowWorkerIsPaidMessages.HirerOptimised.error
      }

      "have the correct error messages for the PutRightAtOwnCostsPage" in {
        Messages("worker.putRightAtOwnCost.error.required") mustBe PutRightAtOwnCostsMessages.WorkerOptimised.error
        Messages("hirer.putRightAtOwnCost.error.required") mustBe PutRightAtOwnCostsMessages.HirerOptimised.error
      }
    }


    "for the Part and Parcel Section" should {

      "have the correct error messages for the BenefitsPage" in {
        Messages("worker.benefits.error.required") mustBe BenefitsMessages.Worker.error
        Messages("hirer.benefits.error.required") mustBe BenefitsMessages.Hirer.error
      }

      "have the correct error messages for the LineManagerDutiesPage" in {
        Messages("worker.lineManagerDuties.error.required") mustBe LineManagerDutiesMessages.Worker.error
        Messages("hirer.lineManagerDuties.error.required") mustBe LineManagerDutiesMessages.Hirer.error
      }

      "have the correct error messages for the IdentifyToStakeholdersPage" in {
        Messages("worker.identifyToStakeholders.error.required") mustBe IdentifyToStakeholdersMessages.Worker.error
        Messages("hirer.identifyToStakeholders.error.required") mustBe IdentifyToStakeholdersMessages.Hirer.error
      }
    }


    "for the Business On Own Account Section" should {

      "have the correct error messages for the WorkerKnownPage" in {
        Messages("workerKnown.error.required") mustBe WorkerKnownMessages.Hirer.error
      }

      "have the correct error messages for the MultipleContractsPage" in {
        Messages("worker.multipleContracts.error.required") mustBe MultipleContractsMessages.Worker.error
        Messages("hirer.multipleContracts.error.required") mustBe MultipleContractsMessages.Hirer.error
      }

      "have the correct error messages for the PermissionToWorkWithOthersPage" in {
        Messages("worker.permissionToWorkWithOthers.error.required") mustBe PermissionToWorkWithOthersMessages.Worker.error
        Messages("hirer.permissionToWorkWithOthers.error.required") mustBe PermissionToWorkWithOthersMessages.Hirer.error
      }

      "have the correct error messages for the OwnershipRightsPage" in {
        Messages("ownershipRights.error.required") mustBe OwnershipRightsMessages.error
      }

      "have the correct error messages for the TransferOfRightsPage" in {
        Messages("worker.transferOfRights.error.required") mustBe TransferOfRightsMessages.Worker.error
        Messages("hirer.transferOfRights.error.required") mustBe TransferOfRightsMessages.Hirer.error
      }

      "have the correct error messages for the RightsOfWorkPage" in {
        Messages("worker.rightsOfWork.error.required") mustBe RightsOfWorkMessages.Worker.error
        Messages("hirer.rightsOfWork.error.required") mustBe RightsOfWorkMessages.Hirer.error
      }

      "have the correct error messages for the PreviousContractPage" in {
        Messages("worker.previousContract.error.required") mustBe PreviousContractMessages.Worker.error
        Messages("hirer.previousContract.error.required") mustBe PreviousContractMessages.Hirer.error
      }

      "have the correct error messages for the FollowOnContractPage" in {
        Messages("followOnContract.error.required") mustBe FollowOnContractMessages.error
      }

      "have the correct error messages for the FirstContractPage" in {
        Messages("worker.firstContract.error.required") mustBe FirstContractMessages.Worker.error
        Messages("hirer.firstContract.error.required") mustBe FirstContractMessages.Hirer.error
      }

      "have the correct error messages for the ExtendContractPage" in {
        Messages("extendContract.error.required") mustBe ExtendContractMessages.error
      }

      "have the correct error messages for the MajorityOfWorkingTimePage" in {
        Messages("worker.majorityOfWorkingTime.error.required") mustBe MajorityOfWorkingTimeMessages.Worker.error
        Messages("hirer.majorityOfWorkingTime.error.required") mustBe MajorityOfWorkingTimeMessages.Hirer.error
      }

      "have the correct error messages for the SimilarWorkOtherClientsPage" in {
        Messages("worker.similarWorkOtherClients.error.required") mustBe SimilarWorkOtherClientsMessages.Worker.error
        Messages("hirer.similarWorkOtherClients.error.required") mustBe SimilarWorkOtherClientsMessages.Hirer.error
      }
    }
  }
}
