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
import models.{BusinessSize, CannotClaimAsExpense, Enumerable, UserAnswers}
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import play.api.i18n.Messages
import play.api.mvc.Request
import viewmodels.AnswerRow
import views.ViewUtils._

class CheckYourAnswersHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  def customisePDF: Option[AnswerRow] = userAnswers.get(CustomisePDFPage) map { x =>
    AnswerRow(
      label = "customisePDF.checkYourAnswersLabel",
      answer = s"${x.answer}",
      answerIsMessageKey = false
    )
  }

  def didPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(DidPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("didPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def rejectSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(RejectSubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("rejectSubstitute.checkYourAnswersLabel"),
        tailorMsg(if(x.answer) "rejectSubstitute.yes" else "rejectSubstitute.no"),
        answerIsMessageKey = true
      )
    }

  def wouldWorkerPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WouldWorkerPaySubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("wouldWorkerPaySubstitute.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def neededToPayHelper(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(NeededToPayHelperPage) map { x =>
      AnswerRow(
        tailorMsg("neededToPayHelper.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def moveWorker(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(MoveWorkerPage) map { x =>
      AnswerRow(
        tailorMsg("moveWorker.checkYourAnswersLabel"),
        tailorMsg(s"moveWorker.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def howWorkIsDone(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkIsDonePage) map { x =>
      AnswerRow(
        tailorMsg("howWorkIsDone.checkYourAnswersLabel"),
        tailorMsg(s"howWorkIsDone.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def scheduleOfWorkingHours(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ScheduleOfWorkingHoursPage) map { x =>
      AnswerRow(
        tailorMsg("scheduleOfWorkingHours.checkYourAnswersLabel"),
        tailorMsg(s"scheduleOfWorkingHours.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def chooseWhereWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ChooseWhereWorkPage) map { x =>
      AnswerRow(
        tailorMsg("chooseWhereWork.checkYourAnswersLabel"),
        tailorMsg(s"chooseWhereWork.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def howWorkerIsPaid(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkerIsPaidPage) map { x =>
      AnswerRow(
        tailorMsg("howWorkerIsPaid.checkYourAnswersLabel"),
        tailorMsg(s"howWorkerIsPaid.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def putRightAtOwnCost(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(PutRightAtOwnCostPage) map { x =>
      AnswerRow(
        tailorMsg("putRightAtOwnCost.checkYourAnswersLabel"),
        tailorMsg(s"putRightAtOwnCost.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def benefits(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(BenefitsPage) map { x =>
      AnswerRow(
        tailorMsg("benefits.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def lineManagerDuties(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(LineManagerDutiesPage) map { x =>
      AnswerRow(
        tailorMsg("lineManagerDuties.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def interactWithStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(InteractWithStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsg("interactWithStakeholders.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def identifyToStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(IdentifyToStakeholdersPage) map { x =>
      AnswerRow(
        tailorMsg("identifyToStakeholders.checkYourAnswersLabel"),
        tailorMsg(s"identifyToStakeholders.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def arrangedSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ArrangedSubstitutePage) map { x =>
      AnswerRow(
        tailorMsg("arrangedSubstitute.checkYourAnswersLabel"),
        tailorMsg(s"arrangedSubstitute.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def cannotClaimAsExpense(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(CannotClaimAsExpensePage) map { x =>
      AnswerRow(
        label = tailorMsg("cannotClaimAsExpense.checkYourAnswersLabel"),
        answers = x.answer.map(ans =>
          AnswerRow(
            label = tailorMsg(s"cannotClaimAsExpense.checkYourAnswersLabel"),
            answer = tailorMsg(s"cannotClaimAsExpense.$ans"),
            answerIsMessageKey = true
          )
        )
      )
    }

  def cannotClaimAsExpenseOptimised(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(CannotClaimAsExpensePage) map { x =>
      AnswerRow(
        label = tailorMsg("cannotClaimAsExpense.checkYourAnswersLabel.optimised"),
        answers = CannotClaimAsExpense.values.map ( expense =>
          AnswerRow(
            label = tailorMsg(s"cannotClaimAsExpense.$expense.checkYourAnswers"),
            if(x.answer.contains(expense)) "site.yes" else "site.no",
            answerIsMessageKey = true
          )
        )
      )
    }

  def businessSize(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] = userAnswers.get(BusinessSizePage).map { x =>
    AnswerRow(
      label = tailorMsg(s"$BusinessSizePage.checkYourAnswersLabel"),
      answers = BusinessSize.values.map ( value =>
        AnswerRow(
          label = s"$BusinessSizePage.$value",
          if(x.answer.contains(value)) "site.yes" else "site.no",
          answerIsMessageKey = true
        )
      )
    )
  }

  def officeHolder(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(OfficeHolderPage) map { x =>
      AnswerRow(
        tailorMsg("officeHolder.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def workerType(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WorkerTypePage) map { x =>
      AnswerRow(
        tailorMsg("workerType.checkYourAnswersLabel"),
        tailorMsg(s"workerType.${x.answer}"),
        answerIsMessageKey = true
      )
    }

  def workerTypeOptimised(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WorkerUsingIntermediaryPage) map { x =>
      AnswerRow(
        tailorMsg(s"$WorkerUsingIntermediaryPage.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def contractStarted(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ContractStartedPage) map { x =>
      AnswerRow(
        tailorMsg("contractStarted.checkYourAnswersLabel"),
        if(x.answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def aboutYou: Option[AnswerRow] = userAnswers.get(AboutYouPage) map { x =>
    AnswerRow(
      "aboutYou.checkYourAnswersLabel",
      s"aboutYou.${x.answer}", answerIsMessageKey = true
    )
  }

  def aboutYouOptimised: Option[AnswerRow] = userAnswers.get(WhichDescribesYouPage) map { x =>
    AnswerRow(
      s"$WhichDescribesYouPage.checkYourAnswersLabel",
      s"$WhichDescribesYouPage.${x.answer}",
      answerIsMessageKey = true
    )
  }

  def isWorkForPrivateSector(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] = userAnswers.get(IsWorkForPrivateSectorPage) map { x =>
    AnswerRow(
      tailorMsg(s"$IsWorkForPrivateSectorPage.checkYourAnswersLabel"),
      if(x.answer) "site.yes" else "site.no",
      answerIsMessageKey = true
    )
  }
}
