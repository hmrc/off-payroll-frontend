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

import models.requests.DataRequest
import viewmodels.AnswerSection

trait UserAnswersUtils {

  def peopleInvolved(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.peopleInvolved.h2"),
    rows = Seq(
      checkYourAnswersHelper.aboutYou,
      checkYourAnswersHelper.contractStarted,
      checkYourAnswersHelper.workerType
    ).flatten,
    useProgressiveDisclosure = true
  )

  def workersDuties(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.workersDuties.h2"),
    rows = Seq(
      checkYourAnswersHelper.officeHolder
    ).flatten,
    useProgressiveDisclosure = true
  )

  def substitutesHelpers(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.substitutesHelpers.h2"),
    rows = Seq(
      checkYourAnswersHelper.arrangedSubstitute,
      checkYourAnswersHelper.didPaySubstitute,
      checkYourAnswersHelper.rejectSubstitute,
      checkYourAnswersHelper.wouldWorkerPaySubstitute,
      checkYourAnswersHelper.neededToPayHelper
    ).flatten,
    useProgressiveDisclosure = true
  )

  def workArrangements(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.workArrangements.h2"),
    rows = Seq(
      checkYourAnswersHelper.moveWorker,
      checkYourAnswersHelper.howWorkIsDone,
      checkYourAnswersHelper.scheduleOfWorkingHours,
      checkYourAnswersHelper.chooseWhereWork
    ).flatten,
    useProgressiveDisclosure = true
  )

  def financialRisk(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.financialRisk.h2"),
    rows = Seq(
      checkYourAnswersHelper.cannotClaimAsExpense,
      checkYourAnswersHelper.howWorkerIsPaid,
      checkYourAnswersHelper.putRightAtOwnCost
    ).flatten,
    useProgressiveDisclosure = true
  )

  def partAndParcel(implicit checkYourAnswersHelper: CheckYourAnswersHelper) = AnswerSection(
    headingKey = Some("result.partAndParcel.h2"),
    rows = Seq(
      checkYourAnswersHelper.benefits,
      checkYourAnswersHelper.lineManagerDuties,
      checkYourAnswersHelper.interactWithStakeholders,
      checkYourAnswersHelper.identifyToStakeholders
    ).flatten,
    useProgressiveDisclosure = true
  )

  def answers(implicit request: DataRequest[_]): Seq[AnswerSection] = {
    implicit val checkYourAnswersHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
    Seq(peopleInvolved,workersDuties,substitutesHelpers,workArrangements,financialRisk,partAndParcel)
  }

}
