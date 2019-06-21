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

package services

import config.FrontendAppConfig
import javax.inject.Inject

import models.requests.DataRequest
import play.api.i18n.Messages
import utils.{CheckYourAnswersHelper, ResultPageHelper}
import viewmodels.AnswerSection

class CheckYourAnswersService @Inject()(implicit val appConfig: FrontendAppConfig) {

  def sections(implicit request: DataRequest[_], messages: Messages): Seq[AnswerSection] = {

    val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    Seq(
      AnswerSection(
        headingKey = Some("checkYourAnswers.setup.header"),
        rows = Seq(
          checkYourAnswersHelper.aboutYouOptimised.map(_ -> None),
          checkYourAnswersHelper.workerTypeOptimised.map(_ -> None),
          checkYourAnswersHelper.isWorkForPrivateSector.map(_ -> None),
          checkYourAnswersHelper.turnoverOver.map(_ -> None),
          checkYourAnswersHelper.employeesOver.map(_ -> None),
          checkYourAnswersHelper.balanceSheetOver.map(_ -> None),
          checkYourAnswersHelper.contractStarted.map(_ -> None)
        ).flatten
      ),
      AnswerSection(
        headingKey = Some("checkYourAnswers.exit.header"),
        rows = Seq(checkYourAnswersHelper.officeHolder.map(_ -> None)).flatten
      ),
      AnswerSection(
        headingKey = Some("checkYourAnswers.personalService.header"),
        rows = Seq(
          checkYourAnswersHelper.arrangedSubstitute.map(_ -> None),
          checkYourAnswersHelper.didPaySubstitute.map(_ -> None),
          checkYourAnswersHelper.rejectSubstitute.map(_ -> None),
          checkYourAnswersHelper.wouldWorkerPaySubstitute.map(_ -> None),
          checkYourAnswersHelper.neededToPayHelper.map(_ -> None)
        ).flatten
      ),
      AnswerSection(
        headingKey = Some("checkYourAnswers.control.header"),
        rows = Seq(
          checkYourAnswersHelper.moveWorker.map(_ -> None),
          checkYourAnswersHelper.howWorkIsDone.map(_ -> None),
          checkYourAnswersHelper.scheduleOfWorkingHours.map(_ -> None),
          checkYourAnswersHelper.chooseWhereWork.map(_ -> None)
        ).flatten
      ),
      AnswerSection(
        headingKey = Some("checkYourAnswers.financialRisk.header"),
        rows = Seq(
          checkYourAnswersHelper.materialsExpenses.map(_ -> None),
          checkYourAnswersHelper.vehicleExpenses.map(_ -> None),
          checkYourAnswersHelper.equipmentExpenses.map(_ -> None),
          checkYourAnswersHelper.otherExpenses.map(_ -> None),
          checkYourAnswersHelper.howWorkerIsPaid.map(_ -> None),
          checkYourAnswersHelper.putRightAtOwnCost.map(_ -> None)
        ).flatten
      ),
      AnswerSection(
        headingKey = Some("checkYourAnswers.partParcel.header"),
        rows = Seq(
          checkYourAnswersHelper.benefits.map(_ -> None),
          checkYourAnswersHelper.lineManagerDuties.map(_ -> None),
          checkYourAnswersHelper.interactWithStakeholders.map(_ -> None),
          checkYourAnswersHelper.identifyToStakeholders.map(_ -> None)
        ).flatten
      )
    )

  }
}
