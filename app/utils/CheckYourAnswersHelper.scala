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
import controllers.sections.setup.{routes => setupRoutes}
import controllers.sections.exit.{routes => exitRoutes}
import controllers.sections.personalService.{routes => personalServiceRoutes}
import controllers.sections.control.{routes => controlRoutes}
import controllers.sections.financialRisk.{routes => financialRiskRoutes}
import controllers.sections.partParcel.{routes => partParcelRoutes}
import models.{CheckMode, Enumerable, UserAnswers}
import pages._
import viewmodels.AnswerRow
import models.Answers._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.i18n.Messages
import play.api.mvc.Request
import views.ViewUtils._

class CheckYourAnswersHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  def customisePDF: Option[AnswerRow] = userAnswers.get(CustomisePDFPage) map {
    x => AnswerRow(
      "customisePDF.checkYourAnswersLabel",
      s"${x.answer}",
      answerIsMessageKey = false,
      routes.PDFController.onPageLoad(CheckMode).url
    )
  }

  def didPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(DidPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("didPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        personalServiceRoutes.DidPaySubstituteController.onPageLoad(CheckMode).url
      )
    }

  def rejectSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(RejectSubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("rejectSubstitute.checkYourAnswersLabel"),
        tailorMsg(if(x.answer) "rejectSubstitute.yes" else "rejectSubstitute.no"),
        answerIsMessageKey = true,
        personalServiceRoutes.RejectSubstituteController.onPageLoad(CheckMode).url
      )
    }

  def wouldWorkerPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WouldWorkerPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("wouldWorkerPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        personalServiceRoutes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url
      )
    }

  def neededToPayHelper(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(NeededToPayHelperPage) map { x =>
      AnswerRow(
        tailorMsg("neededToPayHelper.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        personalServiceRoutes.NeededToPayHelperController.onPageLoad(CheckMode).url
      )
    }

  def moveWorker(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(MoveWorkerPage) map { x =>
      AnswerRow(
        tailorMsg("moveWorker.checkYourAnswersLabel"),
        tailorMsg(s"moveWorker.${x.answer}"),
        answerIsMessageKey = true,
        controlRoutes.MoveWorkerController.onPageLoad(CheckMode).url
      )
    }

  def howWorkIsDone(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkIsDonePage) map { x =>
      AnswerRow(
        tailorMsg("howWorkIsDone.checkYourAnswersLabel"),
        tailorMsg(s"howWorkIsDone.${x.answer}"),
        answerIsMessageKey = true,
        controlRoutes.HowWorkIsDoneController.onPageLoad(CheckMode).url
      )
    }

  def scheduleOfWorkingHours(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ScheduleOfWorkingHoursPage) map { x =>
      AnswerRow(
        tailorMsg("scheduleOfWorkingHours.checkYourAnswersLabel"),
        tailorMsg(s"scheduleOfWorkingHours.${x.answer}"),
        answerIsMessageKey = true,
        controlRoutes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url
      )
    }

  def chooseWhereWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ChooseWhereWorkPage) map { x =>
      AnswerRow(
        tailorMsg("chooseWhereWork.checkYourAnswersLabel"),
        tailorMsg(s"chooseWhereWork.${x.answer}"),
        answerIsMessageKey = true,
        controlRoutes.ChooseWhereWorkController.onPageLoad(CheckMode).url
      )
    }

  def howWorkerIsPaid(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkerIsPaidPage) map { x =>
      AnswerRow(
        tailorMsg("howWorkerIsPaid.checkYourAnswersLabel"),
        tailorMsg(s"howWorkerIsPaid.${x.answer}"),
        answerIsMessageKey = true,
        financialRiskRoutes.HowWorkerIsPaidController.onPageLoad(CheckMode).url
      )
    }

  def putRightAtOwnCost(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(PutRightAtOwnCostPage) map { x =>
      AnswerRow(
        tailorMsg("putRightAtOwnCost.checkYourAnswersLabel"),
        tailorMsg(s"putRightAtOwnCost.${x.answer}"),
        answerIsMessageKey = true,
        financialRiskRoutes.PutRightAtOwnCostController.onPageLoad(CheckMode).url
      )
    }

  def benefits(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(BenefitsPage) map { x =>
      AnswerRow(
        tailorMsg("benefits.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        partParcelRoutes.BenefitsController.onPageLoad(CheckMode).url
      )
    }

  def lineManagerDuties(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(LineManagerDutiesPage) map { x =>
      AnswerRow(
        tailorMsg("lineManagerDuties.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        partParcelRoutes.LineManagerDutiesController.onPageLoad(CheckMode).url
      )
    }

  def interactWithStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(InteractWithStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsg("interactWithStakeholders.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        partParcelRoutes.InteractWithStakeholdersController.onPageLoad(CheckMode).url
      )
    }

  def identifyToStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(IdentifyToStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsg("identifyToStakeholders.checkYourAnswersLabel"),
        tailorMsg(s"identifyToStakeholders.${x.answer}"),
        answerIsMessageKey = true,
        partParcelRoutes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url
      )
    }

  def arrangedSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ArrangedSubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("arrangedSubstitute.checkYourAnswersLabel"),
        tailorMsg(s"arrangedSubstitute.${x.answer}"),
        answerIsMessageKey = true,
        personalServiceRoutes.ArrangedSubstituteController.onPageLoad(CheckMode).url
      )
    }

  def cannotClaimAsExpense(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(CannotClaimAsExpensePage) map { x =>
      AnswerRow(
        label = tailorMsg("cannotClaimAsExpense.checkYourAnswersLabel"),
        answer = x.answer.map(ans => tailorMsg(s"cannotClaimAsExpense.$ans")),
        answerIsMessageKey = true,
        changeUrl = financialRiskRoutes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
      )
    }

  def officeHolder(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(OfficeHolderPage) map { x =>
      AnswerRow(
        tailorMsg("officeHolder.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        exitRoutes.OfficeHolderController.onPageLoad(CheckMode).url
      )
    }

  def workerType(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WorkerTypePage) map { x =>
      AnswerRow(
        tailorMsg("workerType.checkYourAnswersLabel"),
        tailorMsg(s"workerType.${x.answer}"),
        answerIsMessageKey = true,
        setupRoutes.WorkerTypeController.onPageLoad(CheckMode).url
      )
    }

  def contractStarted(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ContractStartedPage) map { x =>
      AnswerRow(
        tailorMsg("contractStarted.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true,
        setupRoutes.ContractStartedController.onPageLoad(CheckMode).url
      )
    }

  def aboutYou: Option[AnswerRow] = userAnswers.get(AboutYouPage) map { x =>
    AnswerRow(
      "aboutYou.checkYourAnswersLabel",
      s"aboutYou.${x.answer}", answerIsMessageKey = true,
      setupRoutes.AboutYouController.onPageLoad(CheckMode).url
    )
  }
}
