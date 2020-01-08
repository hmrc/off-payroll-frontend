/*
 * Copyright 2020 HM Revenue & Customs
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
import models.Section
import models.requests.DataRequest
import play.api.i18n.Messages
import play.twirl.api.Html
import utils.CheckYourAnswersHelper
import viewmodels.{AnswerRow, AnswerSection}

class CheckYourAnswersService @Inject()(implicit val appConfig: FrontendAppConfig) {

  private implicit val convert: Seq[AnswerRow] => Seq[(AnswerRow, Option[Html])] = _.map(_ -> None)

  //noinspection ScalaStyle
  def sections(implicit request: DataRequest[_], messages: Messages): Seq[AnswerSection] = {

    val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

    Seq(
      AnswerSection(
        section = Section.setup,
        headingKey = "checkYourAnswers.setup.header",
        rows = Seq(
          checkYourAnswersHelper.whatDoYouWantToFindOut,
          checkYourAnswersHelper.whoAreYou,
          checkYourAnswersHelper.whatDoYouWantToDo,
          checkYourAnswersHelper.workerUsingIntermediary,
          checkYourAnswersHelper.contractStarted

        ).flatten
      ),
      AnswerSection(
        section = Section.earlyExit,
        headingKey = "checkYourAnswers.exit.header",
        rows = Seq(checkYourAnswersHelper.officeHolder).flatten
      ),
      AnswerSection(
        section = Section.personalService,
        headingKey = "checkYourAnswers.personalService.header",
        rows = Seq(
          checkYourAnswersHelper.arrangedSubstitute,
          checkYourAnswersHelper.didPaySubstitute,
          checkYourAnswersHelper.rejectSubstitute,
          checkYourAnswersHelper.wouldWorkerPaySubstitute,
          checkYourAnswersHelper.neededToPayHelper
        ).flatten
      ),
      AnswerSection(
        section = Section.control,
        headingKey = "checkYourAnswers.control.header",
        rows = Seq(
          checkYourAnswersHelper.moveWorker,
          checkYourAnswersHelper.howWorkIsDone,
          checkYourAnswersHelper.scheduleOfWorkingHours,
          checkYourAnswersHelper.chooseWhereWork
        ).flatten
      ),
      AnswerSection(
        section = Section.financialRisk,
        headingKey = "checkYourAnswers.financialRisk.header",
        rows = Seq(
          checkYourAnswersHelper.equipmentExpenses,
          checkYourAnswersHelper.vehicleExpenses,
          checkYourAnswersHelper.materialsExpenses,
          checkYourAnswersHelper.otherExpenses,
          checkYourAnswersHelper.howWorkerIsPaid,
          checkYourAnswersHelper.putRightAtOwnCost
        ).flatten
      ),
      AnswerSection(
        section = Section.partAndParcel,
        headingKey = "checkYourAnswers.partParcel.header",
        rows = Seq(
          checkYourAnswersHelper.benefits,
          checkYourAnswersHelper.lineManagerDuties,
          checkYourAnswersHelper.identifyToStakeholders
        ).flatten
      )
      ,
      AnswerSection(
        section = Section.businessOnOwnAccount,
        headingKey = "checkYourAnswers.businessOnOwnAccount.header",
        rows = Seq(
          checkYourAnswersHelper.workerKnown,
          checkYourAnswersHelper.multipleContracts,
          checkYourAnswersHelper.permissionToWorkWithOthers,
          checkYourAnswersHelper.ownershipRights,
          checkYourAnswersHelper.rightsOfWork,
          checkYourAnswersHelper.transferOfRights,
          checkYourAnswersHelper.previousContract,
          checkYourAnswersHelper.followOnContract,
          checkYourAnswersHelper.firstContract,
          checkYourAnswersHelper.extendContract,
          checkYourAnswersHelper.majorityOfWorkingTime,
          checkYourAnswersHelper.similarWorkOtherClients
        ).flatten
      )
    )

  }
}
