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
import play.api.i18n.Messages
import viewmodels.AnswerSection
import play.twirl.api.Html
import views.html.components._

trait UserAnswersUtils {

  def peopleInvolved(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.peopleInvolved.h2"),
    whyResult = None,
    rows = Seq(
      (checkYourAnswersHelper.aboutYou, Some(hint_p(Html(messages("aboutYou.hint"))))),
      (checkYourAnswersHelper.contractStarted, None),
      (checkYourAnswersHelper.workerType, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def workersDuties(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.workersDuties.h2"),
    whyResult = Some(Html(messages("result.officeHolderInsideIR35.whyResult.p1"))),
    rows = Seq(
      (checkYourAnswersHelper.officeHolder, Some(office_holder_text.apply()))
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def substitutesHelpers(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.substitutesHelpers.h2"),
    whyResult = Some(Html(messages("personalServiceCluster.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.arrangedSubstitute, None),
      (checkYourAnswersHelper.didPaySubstitute, None),
      (checkYourAnswersHelper.rejectSubstitute, Some(reject_substitute_text.apply())),
      (checkYourAnswersHelper.wouldWorkerPaySubstitute, Some(exclamation(Html(messages("wouldWorkerPaySubstitute.exclamation"))))),
      (checkYourAnswersHelper.neededToPayHelper, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def workArrangements(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.workArrangements.h2"),
    whyResult = Some(Html(messages("controlCluster.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.moveWorker, Some(hint_p(Html(messages("moveWorker.hint"))))),
      (checkYourAnswersHelper.howWorkIsDone, Some(hint_p(Html(messages("howWorkIsDone.hint"))))),
      (checkYourAnswersHelper.scheduleOfWorkingHours, None),
      (checkYourAnswersHelper.chooseWhereWork, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def financialRisk(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.financialRisk.h2"),
     whyResult = Some(Html(messages("financialRiskCluster.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.cannotClaimAsExpense, None),
      (checkYourAnswersHelper.howWorkerIsPaid, None),
      (checkYourAnswersHelper.putRightAtOwnCost, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def partAndParcel(implicit checkYourAnswersHelper: CheckYourAnswersHelper, messages: Messages) = AnswerSection(
    headingKey = Some("result.partAndParcel.h2"),
    whyResult = Some(Html(messages("partParcelCluster.summary"))),
    rows = Seq(
      (checkYourAnswersHelper.benefits, Some(benefits_text.apply())),
      (checkYourAnswersHelper.lineManagerDuties, Some(line_manager_duties.apply())),
      (checkYourAnswersHelper.interactWithStakeholders, None),
      (checkYourAnswersHelper.identifyToStakeholders, None)
    ).filter(_._1.isDefined).map( answer => (answer._1.get, answer._2)),
    useProgressiveDisclosure = true
  )

  def answers(implicit request: DataRequest[_], messages: Messages): Seq[AnswerSection] = {
    implicit val checkYourAnswersHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
    Seq(peopleInvolved,workersDuties,substitutesHelpers,workArrangements,financialRisk,partAndParcel)
  }

}
