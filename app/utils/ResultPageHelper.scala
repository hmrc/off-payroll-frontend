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
import models.{Enumerable, UserAnswers}
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

class ResultPageHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  def didPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(DidPaySubstitutePage) map { answer =>
      AnswerRow(
        tailorMsg("didPaySubstitute.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def rejectSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(RejectSubstitutePage) map { answer =>
      AnswerRow(
        tailorMsg("rejectSubstitute.checkYourAnswersLabel"),
        tailorMsg(if(answer) "rejectSubstitute.yes" else "rejectSubstitute.no"),
        answerIsMessageKey = true
      )
    }

  def wouldWorkerPaySubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WouldWorkerPaySubstitutePage) map { answer =>
      AnswerRow(
        tailorMsg("wouldWorkerPaySubstitute.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def neededToPayHelper(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(NeededToPayHelperPage) map { answer =>
      AnswerRow(
        tailorMsg("neededToPayHelper.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def moveWorker(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(MoveWorkerPage) map { answer =>
      AnswerRow(
        tailorMsg("moveWorker.checkYourAnswersLabel"),
        tailorMsg(s"moveWorker.$answer"),
        answerIsMessageKey = true
      )
    }

  def howWorkIsDone(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkIsDonePage) map { answer =>
      AnswerRow(
        tailorMsg("howWorkIsDone.checkYourAnswersLabel"),
        tailorMsg(s"howWorkIsDone.$answer"),
        answerIsMessageKey = true
      )
    }

  def scheduleOfWorkingHours(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ScheduleOfWorkingHoursPage) map { answer =>
      AnswerRow(
        tailorMsg("scheduleOfWorkingHours.checkYourAnswersLabel"),
        tailorMsg(s"scheduleOfWorkingHours.$answer"),
        answerIsMessageKey = true
      )
    }

  def chooseWhereWork(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ChooseWhereWorkPage) map { answer =>
      AnswerRow(
        tailorMsg("chooseWhereWork.checkYourAnswersLabel"),
        tailorMsg(s"chooseWhereWork.$answer"),
        answerIsMessageKey = true
      )
    }

  def howWorkerIsPaid(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(HowWorkerIsPaidPage) map { answer =>
      AnswerRow(
        tailorMsg("howWorkerIsPaid.checkYourAnswersLabel"),
        tailorMsg(s"howWorkerIsPaid.$answer"),
        answerIsMessageKey = true
      )
    }

  def putRightAtOwnCost(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(PutRightAtOwnCostPage) map { answer =>
      AnswerRow(
        tailorMsg("putRightAtOwnCost.checkYourAnswersLabel"),
        tailorMsg(s"putRightAtOwnCost.$answer"),
        answerIsMessageKey = true
      )
    }

  def benefits(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(BenefitsPage) map { answer =>
      AnswerRow(
        tailorMsg("benefits.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def lineManagerDuties(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(LineManagerDutiesPage) map { answer =>
      AnswerRow(
        tailorMsg("lineManagerDuties.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def interactWithStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(InteractWithStakeholdersPage) map { answer =>
      AnswerRow(
        tailorMsg("interactWithStakeholders.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def identifyToStakeholders(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(IdentifyToStakeholdersPage) map { answer =>
      AnswerRow(
        tailorMsg("identifyToStakeholders.checkYourAnswersLabel"),
        tailorMsg(s"identifyToStakeholders.$answer"),
        answerIsMessageKey = true
      )
    }

  def arrangedSubstitute(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ArrangedSubstitutePage) map { answer =>
      AnswerRow(
        tailorMsg("arrangedSubstitute.checkYourAnswersLabel"),
        tailorMsg(s"arrangedSubstitute.$answer"),
        answerIsMessageKey = true
      )
    }

  def cannotClaimAsExpense(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(CannotClaimAsExpensePage) map { answer =>
      AnswerRow(
        label = tailorMsg("cannotClaimAsExpense.checkYourAnswersLabel"),
        answers = answer.map(ans =>
          AnswerRow(
            label = tailorMsg(s"cannotClaimAsExpense.checkYourAnswersLabel"),
            answer = tailorMsg(s"cannotClaimAsExpense.$ans"),
            answerIsMessageKey = true
          )
        )
      )
    }

  def officeHolder(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(OfficeHolderPage) map { answer =>
      AnswerRow(
        tailorMsg("officeHolder.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def workerType(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(WorkerTypePage) map { answer =>
      AnswerRow(
        tailorMsg("workerType.checkYourAnswersLabel"),
        tailorMsg(s"workerType.$answer"),
        answerIsMessageKey = true
      )
    }

  def contractStarted(implicit messages: Messages, request: Request[_], appConfig: FrontendAppConfig): Option[AnswerRow] =
    userAnswers.get(ContractStartedPage) map { answer =>
      AnswerRow(
        tailorMsg("contractStarted.checkYourAnswersLabel"),
        if(answer) "site.yes" else "site.no",
        answerIsMessageKey = true
      )
    }

  def aboutYou: Option[AnswerRow] = userAnswers.get(AboutYouPage) map { answer =>
    AnswerRow(
      "aboutYou.checkYourAnswersLabel",
      s"aboutYou.$answer", answerIsMessageKey = true
    )
  }
}
