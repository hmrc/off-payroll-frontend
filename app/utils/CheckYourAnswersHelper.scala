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

package utils

import config.FrontendAppConfig
import controllers.routes
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models.{CheckMode, Enumerable, UserAnswers}
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.i18n.Messages
import play.api.mvc.Request
import viewmodels.AnswerRow
import views.ViewUtils._

class CheckYourAnswersHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  def turnoverOver(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(TurnoverOverPage) map { x =>
      AnswerRow(
        tailorMsg(s"$TurnoverOverPage.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
      )
    }

  def employeesOver(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(EmployeesOverPage) map { x =>
      AnswerRow(
        tailorMsg(s"$EmployeesOverPage.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
      )
    }

  def balanceSheetOver(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(BalanceSheetOverPage) map { x =>
      AnswerRow(
        tailorMsg(s"$BalanceSheetOverPage.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
      )
    }

  def didPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(DidPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsgOptimised("didPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(routes.PersonalServiceSectionChangeWarningController.onPageLoad(DidPaySubstitutePage).url)
      )
    }

  def rejectSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(RejectSubstitutePage) map { x =>
      AnswerRow(
        tailorMsgOptimised("rejectSubstitute.checkYourAnswersLabel"),
        if(!x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(routes.PersonalServiceSectionChangeWarningController.onPageLoad(RejectSubstitutePage).url)
      )
    }

  def wouldWorkerPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WouldWorkerPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsgOptimised("wouldWorkerPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(routes.PersonalServiceSectionChangeWarningController.onPageLoad(WouldWorkerPaySubstitutePage).url)
      )
    }

  def neededToPayHelper(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(NeededToPayHelperPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("neededToPayHelper.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(routes.PersonalServiceSectionChangeWarningController.onPageLoad(NeededToPayHelperPage).url)
      )
    }

  def moveWorker(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(MoveWorkerPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("moveWorker.checkYourAnswersLabel"),
        tailorMsgOptimised(s"moveWorker.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url)
      )
    }

  def howWorkIsDone(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkIsDonePage) map { x =>
      AnswerRow(
        tailorMsgOptimised("howWorkIsDone.checkYourAnswersLabel"),
        tailorMsgOptimised(s"howWorkIsDone.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url)
      )
    }

  def scheduleOfWorkingHours(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ScheduleOfWorkingHoursPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("scheduleOfWorkingHours.checkYourAnswersLabel"),
        tailorMsgOptimised(s"scheduleOfWorkingHours.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url)
      )
    }

  def chooseWhereWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ChooseWhereWorkPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("chooseWhereWork.checkYourAnswersLabel"),
        tailorMsgOptimised(s"chooseWhereWork.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url)
      )
    }

  def howWorkerIsPaid(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkerIsPaidPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("howWorkerIsPaid.checkYourAnswersLabel"),
        tailorMsgOptimised(s"howWorkerIsPaid.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url)
      )
    }

  def putRightAtOwnCost(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(PutRightAtOwnCostPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("putRightAtOwnCost.checkYourAnswersLabel"),
        tailorMsgOptimised(s"putRightAtOwnCost.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(CheckMode).url)
      )
    }

  def benefits(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(BenefitsPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("benefits.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url)
      )
    }

  def lineManagerDuties(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(LineManagerDutiesPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("lineManagerDuties.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url)
      )
    }

  def interactWithStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(InteractWithStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("interactWithStakeholders.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url)
      )
    }

  def identifyToStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(IdentifyToStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("identifyToStakeholders.checkYourAnswersLabel"),
        tailorMsgOptimised(s"identifyToStakeholders.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url)
      )
    }

  def arrangedSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ArrangedSubstitutePage) map { x =>
      AnswerRow(
        tailorMsgOptimised("arrangedSubstitute.checkYourAnswersLabel"),
        tailorMsgOptimised(s"arrangedSubstitute.${x.answer}"),
        answerIsMessageKey = true,
        changeUrl = Some(routes.PersonalServiceSectionChangeWarningController.onPageLoad(ArrangedSubstitutePage).url)
      )
    }

  def materialsExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(MaterialsPage) map { x =>
      AnswerRow(
        tailorMsg("materials.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.MaterialsController.onPageLoad(CheckMode).url)
      )
    }

  def vehicleExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(VehiclePage) map { x =>
      AnswerRow(
        tailorMsg("vehicle.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.VehicleController.onPageLoad(CheckMode).url)
      )
    }

  def equipmentExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(EquipmentExpensesPage) map { x =>
      AnswerRow(
        tailorMsg("equipmentExpenses.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.EquipmentExpensesController.onPageLoad(CheckMode).url)
      )
    }

  def otherExpenses(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(OtherExpensesPage) map { x =>
      AnswerRow(
        tailorMsg("otherExpenses.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(financialRiskRoutes.OtherExpensesController.onPageLoad(CheckMode).url)
      )
    }

  def officeHolder(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(OfficeHolderPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("officeHolder.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url)
      )
    }

  def workerTypeOptimised(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WorkerUsingIntermediaryPage) map { x =>
      AnswerRow(
        tailorMsg(s"$WorkerUsingIntermediaryPage.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
      )
    }

  def contractStarted(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ContractStartedPage) map { x =>
      AnswerRow(
        tailorMsgOptimised("contractStarted.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
      )
    }

  def aboutYouOptimised: Option[AnswerRow] = userAnswers.get(WhichDescribesYouPage) map { x =>
    AnswerRow(
      s"$WhichDescribesYouPage.checkYourAnswersLabel",
      s"$WhichDescribesYouPage.${x.answer}",
      answerIsMessageKey = true,
      changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
    )
  }

  def isWorkForPrivateSector(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] = userAnswers.get(IsWorkForPrivateSectorPage) map { x =>
    AnswerRow(
      tailorMsg(s"$IsWorkForPrivateSectorPage.checkYourAnswersLabel"),
      if(x.answer) "isWorkForPrivateSector.private" else "isWorkForPrivateSector.public",
      answerIsMessageKey = true,
      changeUrl = Some(controllers.routes.ResetAnswersWarningController.onPageLoad().url)
    )
  }
}
