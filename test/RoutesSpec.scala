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
import models.{CheckMode, NormalMode, Section}

class RoutesSpec extends SpecBase {

  def fullPath(path: String) = s"/check-employment-status-for-tax$path"

  "App.Routes" should {

    "Have the correct routes for the About Your Result page" in {
      setupRoutes.AboutYourResultController.onPageLoad().url mustBe fullPath("/disclaimer")
      setupRoutes.AboutYourResultController.onSubmit().url mustBe fullPath("/disclaimer")
    }

    "Have the correct routes for the Agency Advisory page" in {
      setupRoutes.AgencyAdvisoryController.onPageLoad().url mustBe fullPath("/agency-not-responsible")
      setupRoutes.AgencyAdvisoryController.onSubmit().url mustBe fullPath("/agency-not-responsible")
    }

    "Have the correct routes for the Contract Started page" in {
      setupRoutes.ContractStartedController.onPageLoad(NormalMode).url mustBe fullPath("/work-started")
      setupRoutes.ContractStartedController.onPageLoad(CheckMode).url mustBe fullPath("/work-started/change")
      setupRoutes.ContractStartedController.onSubmit(NormalMode).url mustBe fullPath("/work-started")
      setupRoutes.ContractStartedController.onSubmit(CheckMode).url mustBe fullPath("/work-started/change")
    }

    "Have the correct routes for the Worker Type page" in {
      setupRoutes.WorkerUsingIntermediaryController.onPageLoad(NormalMode).url mustBe fullPath("/workers-intermediary")
      setupRoutes.WorkerUsingIntermediaryController.onPageLoad(CheckMode).url mustBe fullPath("/workers-intermediary/change")
      setupRoutes.WorkerUsingIntermediaryController.onSubmit(NormalMode).url mustBe fullPath("/workers-intermediary")
      setupRoutes.WorkerUsingIntermediaryController.onSubmit(CheckMode).url mustBe fullPath("/workers-intermediary/change")
    }

    "Have the correct routes for the What Do You Want To Do page" in {
      setupRoutes.WhatDoYouWantToDoController.onPageLoad(NormalMode).url mustBe fullPath("/what-do-you-want-to-do")
      setupRoutes.WhatDoYouWantToDoController.onPageLoad(CheckMode).url mustBe fullPath("/what-do-you-want-to-do/change")
      setupRoutes.WhatDoYouWantToDoController.onSubmit(NormalMode).url mustBe fullPath("/what-do-you-want-to-do")
      setupRoutes.WhatDoYouWantToDoController.onSubmit(CheckMode).url mustBe fullPath("/what-do-you-want-to-do/change")
    }

    "Have the correct routes for the Office Holder page" in {
      exitRoutes.OfficeHolderController.onPageLoad(NormalMode).url mustBe fullPath("/office-holder")
      exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url mustBe fullPath("/office-holder/change")
      exitRoutes.OfficeHolderController.onSubmit(NormalMode).url mustBe fullPath("/office-holder")
      exitRoutes.OfficeHolderController.onSubmit(CheckMode).url mustBe fullPath("/office-holder/change")
    }

    "Have the correct routes for the Arranged Substitute page" in {
      personalServiceRoutes.ArrangedSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/sent-substitute")
      personalServiceRoutes.ArrangedSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/sent-substitute/change")
      personalServiceRoutes.ArrangedSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/sent-substitute")
      personalServiceRoutes.ArrangedSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/sent-substitute/change")
    }

    "Have the correct routes for the Did Pay Substitute page" in {
      personalServiceRoutes.DidPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/paid-substitute")
      personalServiceRoutes.DidPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/paid-substitute/change")
      personalServiceRoutes.DidPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/paid-substitute")
      personalServiceRoutes.DidPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/paid-substitute/change")
    }

    "Have the correct routes for the Reject Substitute page" in {
      personalServiceRoutes.RejectSubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/right-to-reject-substitute")
      personalServiceRoutes.RejectSubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/right-to-reject-substitute/change")
      personalServiceRoutes.RejectSubstituteController.onSubmit(NormalMode).url mustBe fullPath("/right-to-reject-substitute")
      personalServiceRoutes.RejectSubstituteController.onSubmit(CheckMode).url mustBe fullPath("/right-to-reject-substitute/change")
    }

    "Have the correct routes for the Would Pay Substitute page" in {
      personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(NormalMode).url mustBe fullPath("/would-pay-substitute")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url mustBe fullPath("/would-pay-substitute/change")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onSubmit(NormalMode).url mustBe fullPath("/would-pay-substitute")
      personalServiceRoutes.WouldWorkerPaySubstituteController.onSubmit(CheckMode).url mustBe fullPath("/would-pay-substitute/change")
    }

    "Have the correct routes for the Needed to Pay Helper page" in {
      personalServiceRoutes.NeededToPayHelperController.onPageLoad(NormalMode).url mustBe fullPath("/paid-helper")
      personalServiceRoutes.NeededToPayHelperController.onPageLoad(CheckMode).url mustBe fullPath("/paid-helper/change")
      personalServiceRoutes.NeededToPayHelperController.onSubmit(NormalMode).url mustBe fullPath("/paid-helper")
      personalServiceRoutes.NeededToPayHelperController.onSubmit(CheckMode).url mustBe fullPath("/paid-helper/change")
    }

    "Have the correct routes for the Move Worker page" in {
      controlRoutes.MoveWorkerController.onPageLoad(NormalMode).url mustBe fullPath("/worker-move-task")
      controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url mustBe fullPath("/worker-move-task/change")
      controlRoutes.MoveWorkerController.onSubmit(NormalMode).url mustBe fullPath("/worker-move-task")
      controlRoutes.MoveWorkerController.onSubmit(CheckMode).url mustBe fullPath("/worker-move-task/change")
    }

    "Have the correct routes for the How Work is Done page" in {
      controlRoutes.HowWorkIsDoneController.onPageLoad(NormalMode).url mustBe fullPath("/decide-how-work-is-done")
      controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url mustBe fullPath("/decide-how-work-is-done/change")
      controlRoutes.HowWorkIsDoneController.onSubmit(NormalMode).url mustBe fullPath("/decide-how-work-is-done")
      controlRoutes.HowWorkIsDoneController.onSubmit(CheckMode).url mustBe fullPath("/decide-how-work-is-done/change")
    }

    "Have the correct routes for the Schedule of Working Hours page" in {
      controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(NormalMode).url mustBe fullPath("/decide-working-hours")
      controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url mustBe fullPath("/decide-working-hours/change")
      controlRoutes.ScheduleOfWorkingHoursController.onSubmit(NormalMode).url mustBe fullPath("/decide-working-hours")
      controlRoutes.ScheduleOfWorkingHoursController.onSubmit(CheckMode).url mustBe fullPath("/decide-working-hours/change")
    }

    "Have the correct routes for the Choose Where Work page" in {
      controlRoutes.ChooseWhereWorkController.onPageLoad(NormalMode).url mustBe fullPath("/decide-where-work-is-done")
      controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url mustBe fullPath("/decide-where-work-is-done/change")
      controlRoutes.ChooseWhereWorkController.onSubmit(NormalMode).url mustBe fullPath("/decide-where-work-is-done")
      controlRoutes.ChooseWhereWorkController.onSubmit(CheckMode).url mustBe fullPath("/decide-where-work-is-done/change")
    }

    "Have the correct routes for the How Worker is Paid page" in {
      financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(NormalMode).url mustBe fullPath("/how-worker-is-paid")
      financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url mustBe fullPath("/how-worker-is-paid/change")
      financialRiskRoutes.HowWorkerIsPaidController.onSubmit(NormalMode).url mustBe fullPath("/how-worker-is-paid")
      financialRiskRoutes.HowWorkerIsPaidController.onSubmit(CheckMode).url mustBe fullPath("/how-worker-is-paid/change")
    }

    "Have the correct routes for the Put Right at Own Cost page" in {
      financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(NormalMode).url mustBe fullPath("/put-work-right")
      financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(CheckMode).url mustBe fullPath("/put-work-right/change")
      financialRiskRoutes.PutRightAtOwnCostController.onSubmit(NormalMode).url mustBe fullPath("/put-work-right")
      financialRiskRoutes.PutRightAtOwnCostController.onSubmit(CheckMode).url mustBe fullPath("/put-work-right/change")
    }

    "Have the correct routes for the Worker Benefits page" in {
      partParcelRoutes.BenefitsController.onPageLoad(NormalMode).url mustBe fullPath("/corporate-benefits")
      partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url mustBe fullPath("/corporate-benefits/change")
      partParcelRoutes.BenefitsController.onSubmit(NormalMode).url mustBe fullPath("/corporate-benefits")
      partParcelRoutes.BenefitsController.onSubmit(CheckMode).url mustBe fullPath("/corporate-benefits/change")
    }

    "Have the correct routes for the Line Manager Duties page" in {
      partParcelRoutes.LineManagerDutiesController.onPageLoad(NormalMode).url mustBe fullPath("/management-responsibilities")
      partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url mustBe fullPath("/management-responsibilities/change")
      partParcelRoutes.LineManagerDutiesController.onSubmit(NormalMode).url mustBe fullPath("/management-responsibilities")
      partParcelRoutes.LineManagerDutiesController.onSubmit(CheckMode).url mustBe fullPath("/management-responsibilities/change")
    }

    "Have the correct routes for the Identify to Stakeholders page" in {
      partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(NormalMode).url mustBe fullPath("/introduce-worker")
      partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url mustBe fullPath("/introduce-worker/change")
      partParcelRoutes.IdentifyToStakeholdersController.onSubmit(NormalMode).url mustBe fullPath("/introduce-worker")
      partParcelRoutes.IdentifyToStakeholdersController.onSubmit(CheckMode).url mustBe fullPath("/introduce-worker/change")
    }

    "Have the correct routes for the Result page" in {
      ResultController.onPageLoad().url mustBe fullPath("/determination")
      ResultController.onSubmit().url mustBe fullPath("/determination")
    }

    "Have the correct routes for the Customise Result page" in {
      PDFController.onPageLoad(NormalMode).url mustBe fullPath("/add-details")
      PDFController.onPageLoad(CheckMode).url mustBe fullPath("/add-details/change")
      PDFController.onSubmit(NormalMode).url mustBe fullPath("/add-details")
      PDFController.onSubmit(CheckMode).url mustBe fullPath("/add-details/change")
    }

    "Have the correct routes for the Check Your Answers page" in {
      CheckYourAnswersController.onPageLoad().url mustBe fullPath("/review-your-answers")
      CheckYourAnswersController.onPageLoad(Some(Section.personalService)).url mustBe fullPath("/review-your-answers?sectionToExpand=personalService")
      CheckYourAnswersController.onSubmit().url mustBe fullPath("/review-your-answers")
    }

    "Have the correct routes for the ExitSurvey" in {
      ExitSurveyController.redirectToExitSurvey().url mustBe fullPath("/exit-survey")
    }
  }
}
