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

import controllers.routes
import models.{CheckMode, Enumerable, UserAnswers}
import pages._
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers) extends Enumerable.Implicits {

  def customisePDF: Option[AnswerRow] = userAnswers.get(CustomisePDFPage) map {
    x => AnswerRow("customisePDF.checkYourAnswersLabel", s"$x", false, routes.PDFController.onPageLoad(CheckMode).url)
  }

  def didPaySubstitute: Option[AnswerRow] = userAnswers.get(DidPaySubstitutePage) map {
    x => AnswerRow("didPaySubstitute.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.DidPaySubstituteController.onPageLoad(CheckMode).url)
  }

  def rejectSubstitute: Option[AnswerRow] = userAnswers.get(RejectSubstitutePage) map {
    x => AnswerRow("rejectSubstitute.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.RejectSubstituteController.onPageLoad(CheckMode).url)
  }

  def wouldWorkerPaySubstitute: Option[AnswerRow] = userAnswers.get(WouldWorkerPaySubstitutePage) map {
    x => AnswerRow("wouldWorkerPaySubstitute.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.WouldWorkerPaySubstituteController.onPageLoad(CheckMode).url)
  }

  def neededToPayHelper: Option[AnswerRow] = userAnswers.get(NeededToPayHelperPage) map {
    x => AnswerRow("neededToPayHelper.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.NeededToPayHelperController.onPageLoad(CheckMode).url)
  }

  def moveWorker: Option[AnswerRow] = userAnswers.get(MoveWorkerPage) map {
    x => AnswerRow("moveWorker.checkYourAnswersLabel", s"moveWorker.$x", true, routes.MoveWorkerController.onPageLoad(CheckMode).url)
  }

  def howWorkIsDone: Option[AnswerRow] = userAnswers.get(HowWorkIsDonePage) map {
    x => AnswerRow("howWorkIsDone.checkYourAnswersLabel", s"howWorkIsDone.$x", true, routes.HowWorkIsDoneController.onPageLoad(CheckMode).url)
  }

  def scheduleOfWorkingHours: Option[AnswerRow] = userAnswers.get(ScheduleOfWorkingHoursPage) map {
    x => AnswerRow("scheduleOfWorkingHours.checkYourAnswersLabel", s"scheduleOfWorkingHours.$x", true, routes.ScheduleOfWorkingHoursController.onPageLoad(CheckMode).url)
  }

  def chooseWhereWork: Option[AnswerRow] = userAnswers.get(ChooseWhereWorkPage) map {
    x => AnswerRow("chooseWhereWork.checkYourAnswersLabel", s"chooseWhereWork.$x", true, routes.ChooseWhereWorkController.onPageLoad(CheckMode).url)
  }

  def howWorkerIsPaid: Option[AnswerRow] = userAnswers.get(HowWorkerIsPaidPage) map {
    x => AnswerRow("howWorkerIsPaid.checkYourAnswersLabel", s"howWorkerIsPaid.$x", true, routes.HowWorkerIsPaidController.onPageLoad(CheckMode).url)
  }

  def putRightAtOwnCost: Option[AnswerRow] = userAnswers.get(PutRightAtOwnCostPage) map {
    x => AnswerRow("putRightAtOwnCost.checkYourAnswersLabel", s"putRightAtOwnCost.$x", true, routes.PutRightAtOwnCostController.onPageLoad(CheckMode).url)
  }

  def benefits: Option[AnswerRow] = userAnswers.get(BenefitsPage) map {
    x => AnswerRow("benefits.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.BenefitsController.onPageLoad(CheckMode).url)
  }

  def lineManagerDuties: Option[AnswerRow] = userAnswers.get(LineManagerDutiesPage) map {
    x => AnswerRow("lineManagerDuties.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.LineManagerDutiesController.onPageLoad(CheckMode).url)
  }

  def interactWithStakeholders: Option[AnswerRow] = userAnswers.get(InteractWithStakeholdersPage) map {
    x => AnswerRow("interactWithStakeholders.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.InteractWithStakeholdersController.onPageLoad(CheckMode).url)
  }

  def identifyToStakeholders: Option[AnswerRow] = userAnswers.get(IdentifyToStakeholdersPage) map {
    x => AnswerRow("identifyToStakeholders.checkYourAnswersLabel", s"identifyToStakeholders.$x", true, routes.IdentifyToStakeholdersController.onPageLoad(CheckMode).url)
  }

  def arrangedSubstitue: Option[AnswerRow] = userAnswers.get(ArrangedSubstituePage) map {
    x => AnswerRow("arrangedSubstitue.checkYourAnswersLabel", s"arrangedSubstitue.$x", true, routes.ArrangedSubstitueController.onPageLoad(CheckMode).url)
  }

  def cannotClaimAsExpense: Option[AnswerRow] = userAnswers.get(CannotClaimAsExpensePage) map {
    x => AnswerRow(
      label = "cannotClaimAsExpense.checkYourAnswersLabel",
      answer = x.map(ans => s"cannotClaimAsExpense.$ans"),
      answerIsMessageKey = true,
      changeUrl = routes.CannotClaimAsExpenseController.onPageLoad(CheckMode).url
    )
  }

  def officeHolder: Option[AnswerRow] = userAnswers.get(OfficeHolderPage) map {
    x => AnswerRow("officeHolder.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.OfficeHolderController.onPageLoad(CheckMode).url)
  }

  def workerType: Option[AnswerRow] = userAnswers.get(WorkerTypePage) map {
    x => AnswerRow("workerType.checkYourAnswersLabel", s"workerType.$x", true, routes.WorkerTypeController.onPageLoad(CheckMode).url)
  }

  def contractStarted: Option[AnswerRow] = userAnswers.get(ContractStartedPage) map {
    x => AnswerRow("contractStarted.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.ContractStartedController.onPageLoad(CheckMode).url)
  }

  def aboutYou: Option[AnswerRow] = userAnswers.get(AboutYouPage) map {
    x => AnswerRow("aboutYou.checkYourAnswersLabel", s"aboutYou.$x", answerIsMessageKey = true, routes.AboutYouController.onPageLoad(CheckMode).url)
  }
}
