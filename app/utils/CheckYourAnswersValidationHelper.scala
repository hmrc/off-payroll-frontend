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

import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.UserAnswers
import pages._
import pages.sections.personalService._
import pages.sections.setup._

trait CheckYourAnswersValidationHelper {

  def privateSectorPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(WorkerUsingIntermediaryPage) match {
      case Some(true) => Set(IsWorkForPrivateSectorPage)
      case _ => Set()
    }
  }

  def turnoverEmployeesPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(IsWorkForPrivateSectorPage) match {
      case Some(true) => Set(TurnoverOverPage, EmployeesOverPage)
      case _ => Set()
    }
  }

  def balanceSheetPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(TurnoverOverPage), userAnswers.getAnswer(EmployeesOverPage)) match {
      case (Some(x), Some(y)) if x != y => Set(BalanceSheetOverPage)
      case _ => Set()
    }
  }

  def arrangedRejectedPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(ContractStartedPage) match {
      case Some(true) => Set(ArrangedSubstitutePage)
      case Some(false) => Set(RejectSubstitutePage)
      case _ => Set()
    }
  }

  def didPayRejectedNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(ArrangedSubstitutePage) match {
      case Some(YesClientAgreed) => Set(DidPaySubstitutePage)
      case Some(No) => Set(RejectSubstitutePage)
      case Some(YesClientNotAgreed) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(WouldWorkerPaySubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
      case (Some(false), Some(true)) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }

  def contractNeededWouldPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    (userAnswers.getAnswer(RejectSubstitutePage), userAnswers.getAnswer(ContractStartedPage)) match {
      case (Some(true), Some(true)) => Set(NeededToPayHelperPage)
      case (Some(false), _) => Set(WouldWorkerPaySubstitutePage)
      case _ => Set()
    }
  }

  def neededPages(implicit userAnswers: UserAnswers): Set[QuestionPage[_]] = {
    userAnswers.getAnswer(DidPaySubstitutePage) match {
      case Some(false) => Set(NeededToPayHelperPage)
      case _ => Set()
    }
  }
}
