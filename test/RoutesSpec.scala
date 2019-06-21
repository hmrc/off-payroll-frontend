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
import controllers.sections.setup.{routes => setupRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models.{CheckMode, NormalMode}

class RoutesSpec extends SpecBase {

  def fullPath(path: String) = s"/check-employment-status-for-tax$path"

  "App.Routes" should {

    "Have the correct routes for the About Your Result page" in {
      setupRoutes.AboutYourResultController.onPageLoad().url mustBe fullPath("/about-your-result")
      setupRoutes.AboutYourResultController.onSubmit().url mustBe fullPath("/about-your-result")
    }

    "Have the correct routes for the Agency Advisory page" in {
      setupRoutes.AgencyAdvisoryController.onPageLoad().url mustBe fullPath("/agency-advisory")
      setupRoutes.AgencyAdvisoryController.onSubmit().url mustBe fullPath("/agency-advisory")
    }

    "Have the correct routes for the Hirer Advisory page" in {
      setupRoutes.HirerAdvisoryController.onPageLoad().url mustBe fullPath("/hirer-advisory")
      setupRoutes.HirerAdvisoryController.onSubmit().url mustBe fullPath("/hirer-advisory")
    }

    "Have the correct routes for the About You page" in {
      setupRoutes.AboutYouController.onPageLoad(NormalMode).url mustBe fullPath("/about-you")
      setupRoutes.AboutYouController.onPageLoad(CheckMode).url mustBe fullPath("/about-you/edit")
      setupRoutes.AboutYouController.onSubmit(NormalMode).url mustBe fullPath("/about-you")
      setupRoutes.AboutYouController.onSubmit(CheckMode).url mustBe fullPath("/about-you/edit")
    }

    "Have the correct routes for the Contract Started page" in {
      setupRoutes.ContractStartedController.onPageLoad(NormalMode).url mustBe fullPath("/work-started")
      setupRoutes.ContractStartedController.onPageLoad(CheckMode).url mustBe fullPath("/work-started/edit")
      setupRoutes.ContractStartedController.onSubmit(NormalMode).url mustBe fullPath("/work-started")
      setupRoutes.ContractStartedController.onSubmit(CheckMode).url mustBe fullPath("/work-started/edit")
    }

    "Have the correct routes for the Worker Type page" in {
      setupRoutes.WorkerTypeController.onPageLoad(NormalMode).url mustBe fullPath("/worker-trading-as")
      setupRoutes.WorkerTypeController.onPageLoad(CheckMode).url mustBe fullPath("/worker-trading-as/edit")
      setupRoutes.WorkerTypeController.onSubmit(NormalMode).url mustBe fullPath("/worker-trading-as")
      setupRoutes.WorkerTypeController.onSubmit(CheckMode).url mustBe fullPath("/worker-trading-as/edit")
    }

    "Have the correct routes for the Tool Not Needed page" in {
      setupRoutes.ToolNotNeededController.onPageLoad().url mustBe fullPath("/tool-not-needed")
      setupRoutes.ToolNotNeededController.onSubmit().url mustBe fullPath("/tool-not-needed")
    }

    "Have the correct routes for the Office Holder page" in {
      exitRoutes.OfficeHolderController.onPageLoad(NormalMode).url mustBe fullPath("/office-holder-duties")
      exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url mustBe fullPath("/office-holder-duties/edit")
      exitRoutes.OfficeHolderController.onSubmit(NormalMode).url mustBe fullPath("/office-holder-duties")
      exitRoutes.OfficeHolderController.onSubmit(CheckMode).url mustBe fullPath("/office-holder-duties/edit")
    }

    "Have the correct routes for the Arranged Substitute page" in {
      personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-sent-substitute")
      personalServiceRoutes.ArrangedSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-sent-substitute/edit")
      personalServiceRoutes.ArrangedSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-sent-substitute")
      personalServiceRoutes.ArrangedSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-sent-substitute/edit")
    }

    "Have the correct routes for the Did Pay Substitute page" in {
      personalServiceRoutes.DidPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-paid-substitute")
      personalServiceRoutes.DidPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-paid-substitute/edit")
      personalServiceRoutes.DidPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-paid-substitute")
      personalServiceRoutes.DidPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-paid-substitute/edit")
    }

    "Have the correct routes for the Reject Substitute page" in {
      personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/substitute-rejected")
      personalServiceRoutes.RejectSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/substitute-rejected/edit")
      personalServiceRoutes.RejectSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/substitute-rejected")
      personalServiceRoutes.RejectSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/substitute-rejected/edit")
    }

    "Have the correct routes for the Would Pay Substitute page" in {
      personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/worker-would-pay-substitute")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/worker-would-pay-substitute/edit")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/worker-would-pay-substitute")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/worker-would-pay-substitute/edit")
    }

    "Have the correct routes for the Needed to Pay Helper page" in {
      personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode).url mustBe fullPath("/worker-paid-helper")
      personalServiceRoutes.NeededToPayHelperController.onPageLoad(CheckMode).url mustBe fullPath("/worker-paid-helper/edit")
      personalServiceRoutes.NeededToPayHelperController.onSubmit(NormalMode).url mustBe fullPath("/worker-paid-helper")
      personalServiceRoutes.NeededToPayHelperController.onSubmit(CheckMode).url mustBe fullPath("/worker-paid-helper/edit")
    }

    "Have the correct routes for the Move Worker page" in {
      controlRoutes.MoveWorkerController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-role")
      controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-role/edit")
      controlRoutes.MoveWorkerController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-role")
      controlRoutes.MoveWorkerController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-role/edit")
    }

    "Have the correct routes for the How Work is Done page" in {
      controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode).url mustBe fullPath("/change-how-work-is-done")
      controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url mustBe fullPath("/change-how-work-is-done/edit")
      controlRoutes.HowWorkIsDoneController.onSubmit(NormalMode).url mustBe fullPath("/change-how-work-is-done")
      controlRoutes.HowWorkIsDoneController.onSubmit(CheckMode).url mustBe fullPath("/change-how-work-is-done/edit")
    }

    "Have the correct routes for the Schedule of Working Hours page" in {
      controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-hours")
      controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-hours/edit")
      controlRoutes.ScheduleOfWorkingHoursController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-hours")
      controlRoutes.ScheduleOfWorkingHoursController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-hours/edit")
    }

    "Have the correct routes for the Choose Where Work page" in {
      controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode).url mustBe fullPath("/change-worker-location")
      controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url mustBe fullPath("/change-worker-location/edit")
      controlRoutes.ChooseWhereWorkController.onSubmit(NormalMode).url mustBe fullPath("/change-worker-location")
      controlRoutes.ChooseWhereWorkController.onSubmit(CheckMode).url mustBe fullPath("/change-worker-location/edit")
    }

    "Have the correct routes for the Cannot Claim as Expense page" in {
      financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(NormalMode).url mustBe fullPath("/worker-cannot-claim")
      financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url mustBe fullPath("/worker-cannot-claim/edit")
      financialRiskRoutes.CannotClaimAsExpenseController.onSubmit(NormalMode).url mustBe fullPath("/worker-cannot-claim")
      financialRiskRoutes.CannotClaimAsExpenseController.onSubmit(CheckMode).url mustBe fullPath("/worker-cannot-claim/edit")
    }

    "Have the correct routes for the How Worker is Paid page" in {
      financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode).url mustBe fullPath("/worker-compensation")
      financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url mustBe fullPath("/worker-compensation/edit")
      financialRiskRoutes.HowWorkerIsPaidController.onSubmit(NormalMode).url mustBe fullPath("/worker-compensation")
      financialRiskRoutes.HowWorkerIsPaidController.onSubmit(CheckMode).url mustBe fullPath("/worker-compensation/edit")
    }

    "Have the correct routes for the Put Right at Own Cost page" in {
      financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode).url mustBe fullPath("/put-work-right")
      financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(CheckMode).url mustBe fullPath("/put-work-right/edit")
      financialRiskRoutes.PutRightAtOwnCostController.onSubmit(NormalMode).url mustBe fullPath("/put-work-right")
      financialRiskRoutes.PutRightAtOwnCostController.onSubmit(CheckMode).url mustBe fullPath("/put-work-right/edit")
    }

    "Have the correct routes for the Worker Benefits page" in {
      partParcelRoutes.BenefitsController.onPageLoad(NormalMode).url mustBe fullPath("/worker-benefits")
      partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url mustBe fullPath("/worker-benefits/edit")
      partParcelRoutes.BenefitsController.onSubmit(NormalMode).url mustBe fullPath("/worker-benefits")
      partParcelRoutes.BenefitsController.onSubmit(CheckMode).url mustBe fullPath("/worker-benefits/edit")
    }

    "Have the correct routes for the Line Manager Duties page" in {
      partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode).url mustBe fullPath("/manager-duties")
      partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url mustBe fullPath("/manager-duties/edit")
      partParcelRoutes.LineManagerDutiesController.onSubmit(NormalMode).url mustBe fullPath("/manager-duties")
      partParcelRoutes.LineManagerDutiesController.onSubmit(CheckMode).url mustBe fullPath("/manager-duties/edit")
    }

    "Have the correct routes for the Interact With Stakeholders page" in {
      partParcelRoutes.InteractWithStakeholdersController.onPageLoad(NormalMode).url mustBe fullPath("/external-interaction")
      partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url mustBe fullPath("/external-interaction/edit")
      partParcelRoutes.InteractWithStakeholdersController.onSubmit(NormalMode).url mustBe fullPath("/external-interaction")
      partParcelRoutes.InteractWithStakeholdersController.onSubmit(CheckMode).url mustBe fullPath("/external-interaction/edit")
    }

    "Have the correct routes for the Identify to Stakeholders page" in {
      partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode).url mustBe fullPath("/identify-worker")
      partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url mustBe fullPath("/identify-worker/edit")
      partParcelRoutes.IdentifyToStakeholdersController.onSubmit(NormalMode).url mustBe fullPath("/identify-worker")
      partParcelRoutes.IdentifyToStakeholdersController.onSubmit(CheckMode).url mustBe fullPath("/identify-worker/edit")
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

    "Have the correct routes for the Leave page" in {
      setupRoutes.LeaveController.onPageLoad().url mustBe fullPath("/you-have-left-the-tool")
    }
  }

}
