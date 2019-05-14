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

import base.SpecBase
import controllers.routes._
import models.{CheckMode, NormalMode}

class RoutesSpec extends SpecBase {

  def fullPath(path: String) = s"/check-employment-status-for-tax$path"

  "App.Routes" should {

    "Have the correct routes for the About You page" in {
      AboutYouController.onPageLoad(NormalMode).url mustBe fullPath("/about-you")
      AboutYouController.onPageLoad(CheckMode).url mustBe fullPath("/about-you/edit")
      AboutYouController.onSubmit(NormalMode).url mustBe fullPath("/about-you")
      AboutYouController.onSubmit(CheckMode).url mustBe fullPath("/about-you/edit")
    }

    "Have the correct routes for the Contract Started page" in {
      ContractStartedController.onPageLoad(NormalMode).url mustBe fullPath("/work-started")
      ContractStartedController.onPageLoad(CheckMode).url mustBe fullPath("/work-started/edit")
      ContractStartedController.onSubmit(NormalMode).url mustBe fullPath("/work-started")
      ContractStartedController.onSubmit(CheckMode).url mustBe fullPath("/work-started/edit")
    }

    "Have the correct routes for the Worker Type page" in {
      WorkerTypeController.onPageLoad(NormalMode).url mustBe fullPath("/worker-trading-as")
      WorkerTypeController.onPageLoad(CheckMode).url mustBe fullPath("/worker-trading-as/edit")
      WorkerTypeController.onSubmit(NormalMode).url mustBe fullPath("/worker-trading-as")
      WorkerTypeController.onSubmit(CheckMode).url mustBe fullPath("/worker-trading-as/edit")
    }

    "Have the correct routes for the Office Holder page" in {
      OfficeHolderController.onPageLoad(NormalMode).url mustBe fullPath("/office-holder-duties")
      OfficeHolderController.onPageLoad(CheckMode).url mustBe fullPath("/office-holder-duties/edit")
      OfficeHolderController.onSubmit(NormalMode).url mustBe fullPath("/office-holder-duties")
      OfficeHolderController.onSubmit(CheckMode).url mustBe fullPath("/office-holder-duties/edit")
    }

    "Have the correct routes for the Arranged Substitute page" in {
      ArrangedSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-sent-substitute")
      ArrangedSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-sent-substitute/edit")
      ArrangedSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-sent-substitute")
      ArrangedSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-sent-substitute/edit")
    }

    "Have the correct routes for the Did Pay Substitute page" in {
      DidPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-paid-substitute")
      DidPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-paid-substitute/edit")
      DidPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-paid-substitute")
      DidPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-paid-substitute/edit")
    }

    "Have the correct routes for the Reject Substitute page" in {
      RejectSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/substitute-rejected")
      RejectSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/substitute-rejected/edit")
      RejectSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/substitute-rejected")
      RejectSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/substitute-rejected/edit")
    }

    "Have the correct routes for the Would Pay Substitute page" in {
      WouldWorkerPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-would-pay-substitute")
      WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-would-pay-substitute/edit")
      WouldWorkerPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-would-pay-substitute")
      WouldWorkerPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-would-pay-substitute/edit")
    }

    "Have the correct routes for the Needed to Pay Helper page" in {
      NeededToPayHelperController.onPageLoad(NormalMode).url mustBe fullPath("/worker-paid-helper")
      NeededToPayHelperController.onPageLoad(CheckMode).url mustBe fullPath("/worker-paid-helper/edit")
      NeededToPayHelperController.onSubmit(NormalMode).url mustBe fullPath("/worker-paid-helper")
      NeededToPayHelperController.onSubmit(CheckMode).url mustBe fullPath("/worker-paid-helper/edit")
    }

    "Have the correct routes for the Move Worker page" in {
      MoveWorkerController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-role")
      MoveWorkerController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-role/edit")
      MoveWorkerController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-role")
      MoveWorkerController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-role/edit")
    }

    "Have the correct routes for the How Work is Done page" in {
      HowWorkIsDoneController.onPageLoad(NormalMode).url mustBe fullPath("/change-how-work-is-done")
      HowWorkIsDoneController.onPageLoad(CheckMode).url mustBe fullPath("/change-how-work-is-done/edit")
      HowWorkIsDoneController.onSubmit(NormalMode).url mustBe fullPath("/change-how-work-is-done")
      HowWorkIsDoneController.onSubmit(CheckMode).url mustBe fullPath("/change-how-work-is-done/edit")
    }

    "Have the correct routes for the Schedule of Working Hours page" in {
      ScheduleOfWorkingHoursController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-hours")
      ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-hours/edit")
      ScheduleOfWorkingHoursController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-hours")
      ScheduleOfWorkingHoursController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-hours/edit")
    }

    "Have the correct routes for the Choose Where Work page" in {
      ChooseWhereWorkController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-location")
      ChooseWhereWorkController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-location/edit")
      ChooseWhereWorkController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-location")
      ChooseWhereWorkController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-location/edit")
    }

    "Have the correct routes for the Cannot Claim as Expense page" in {
      CannotClaimAsExpenseController.onPageLoad(NormalMode).url mustBe fullPath("/worker-cannot-claim")
      CannotClaimAsExpenseController.onPageLoad(CheckMode).url mustBe fullPath("/worker-cannot-claim/edit")
      CannotClaimAsExpenseController.onSubmit(NormalMode).url mustBe fullPath("/worker-cannot-claim")
      CannotClaimAsExpenseController.onSubmit(CheckMode).url mustBe fullPath("/worker-cannot-claim/edit")
    }

    "Have the correct routes for the How Worker is Paid page" in {
      HowWorkerIsPaidController.onPageLoad(NormalMode).url mustBe fullPath("/worker-compensation")
      HowWorkerIsPaidController.onPageLoad(CheckMode).url mustBe fullPath("/worker-compensation/edit")
      HowWorkerIsPaidController.onSubmit(NormalMode).url mustBe fullPath("/worker-compensation")
      HowWorkerIsPaidController.onSubmit(CheckMode).url mustBe fullPath("/worker-compensation/edit")
    }

    "Have the correct routes for the Put Right at Own Cost page" in {
      PutRightAtOwnCostController.onPageLoad(NormalMode).url mustBe fullPath("/put-work-right")
      PutRightAtOwnCostController.onPageLoad(CheckMode).url mustBe fullPath("/put-work-right/edit")
      PutRightAtOwnCostController.onSubmit(NormalMode).url mustBe fullPath("/put-work-right")
      PutRightAtOwnCostController.onSubmit(CheckMode).url mustBe fullPath("/put-work-right/edit")
    }

    "Have the correct routes for the Worker Benefits page" in {
      BenefitsController.onPageLoad(NormalMode).url mustBe fullPath("/worker-benefits")
      BenefitsController.onPageLoad(CheckMode).url mustBe fullPath("/worker-benefits/edit")
      BenefitsController.onSubmit(NormalMode).url mustBe fullPath("/worker-benefits")
      BenefitsController.onSubmit(CheckMode).url mustBe fullPath("/worker-benefits/edit")
    }

    "Have the correct routes for the Line Manager Duties page" in {
      LineManagerDutiesController.onPageLoad(NormalMode).url mustBe fullPath("/manager-duties")
      LineManagerDutiesController.onPageLoad(CheckMode).url mustBe fullPath("/manager-duties/edit")
      LineManagerDutiesController.onSubmit(NormalMode).url mustBe fullPath("/manager-duties")
      LineManagerDutiesController.onSubmit(CheckMode).url mustBe fullPath("/manager-duties/edit")
    }

    "Have the correct routes for the Interact With Stakeholders page" in {
      InteractWithStakeholdersController.onPageLoad(NormalMode).url mustBe fullPath("/external-interaction")
      InteractWithStakeholdersController.onPageLoad(CheckMode).url mustBe fullPath("/external-interaction/edit")
      InteractWithStakeholdersController.onSubmit(NormalMode).url mustBe fullPath("/external-interaction")
      InteractWithStakeholdersController.onSubmit(CheckMode).url mustBe fullPath("/external-interaction/edit")
    }

    "Have the correct routes for the Identify to Stakeholders page" in {
      IdentifyToStakeholdersController.onPageLoad(NormalMode).url mustBe fullPath("/identify-worker")
      IdentifyToStakeholdersController.onPageLoad(CheckMode).url mustBe fullPath("/identify-worker/edit")
      IdentifyToStakeholdersController.onSubmit(NormalMode).url mustBe fullPath("/identify-worker")
      IdentifyToStakeholdersController.onSubmit(CheckMode).url mustBe fullPath("/identify-worker/edit")
    }

    "Have the correct routes for the Result page" in {
      ResultController.onPageLoad().url mustBe fullPath("/your-result")
      ResultController.onSubmit().url mustBe fullPath("/your-result")
    }

    "Have the correct routes for the Customise Result page" in {
      PDFController.onPageLoad(NormalMode).url mustBe fullPath("/add-details-for-reference")
      PDFController.onPageLoad(CheckMode).url mustBe fullPath("/add-details-for-reference/edit")
      PDFController.onSubmit(NormalMode).url mustBe fullPath("/add-details-for-reference")
      PDFController.onSubmit(CheckMode).url mustBe fullPath("/add-details-for-reference/edit")
    }
  }

}
