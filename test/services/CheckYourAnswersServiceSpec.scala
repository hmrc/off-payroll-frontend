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

import base.GuiceAppSpecBase
import models._
import models.requests.DataRequest
import models.sections.control.ChooseWhereWork.WorkerChooses
import models.sections.control.HowWorkIsDone.NoWorkerInputAllowed
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.ScheduleDecidedForWorker
import models.sections.financialRisk.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.sections.financialRisk.PutRightAtOwnCost.OutsideOfHoursNoCosts
import models.sections.partAndParcel.IdentifyToStakeholders.WorkForEndClient
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhatDoYouWantToFindOut.IR35
import models.sections.setup.WhoAreYou.Worker
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection

class CheckYourAnswersServiceSpec extends GuiceAppSpecBase {

  object CheckYourAnswersService extends CheckYourAnswersService

  "CheckYourAnswersService.sections" must {

    "when all questions are in the CacheMap" must {

      lazy val userAnswers: UserAnswers = UserAnswers("id")
        //Setup Section
        .set(WhatDoYouWantToFindOutPage, IR35)
        .set(WhoAreYouPage, Worker)
        .set(WhatDoYouWantToDoPage, MakeNewDetermination)
        .set(WorkerUsingIntermediaryPage, true)
        .set(ContractStartedPage, true)
        //Exit Section
        .set(OfficeHolderPage, true)
        //Personal Service section
        .set(ArrangedSubstitutePage, YesClientAgreed)
        .set(DidPaySubstitutePage, true)
        .set(RejectSubstitutePage, true)
        .set(WouldWorkerPaySubstitutePage, true)
        .set(NeededToPayHelperPage, true)
        //Control section
        .set(ChooseWhereWorkPage, WorkerChooses)
        .set(HowWorkIsDonePage, NoWorkerInputAllowed)
        .set(MoveWorkerPage, CanMoveWorkerWithPermission)
        .set(ScheduleOfWorkingHoursPage, ScheduleDecidedForWorker)
        //Financial Risk section
        .set(MaterialsPage, true)
        .set(VehiclePage, true)
        .set(EquipmentExpensesPage, true)
        .set(OtherExpensesPage, true)
        .set(HowWorkerIsPaidPage, HourlyDailyOrWeekly)
        .set(PutRightAtOwnCostPage, OutsideOfHoursNoCosts)
        //Part and Parcel section
        .set(BenefitsPage, false)
        .set(LineManagerDutiesPage, false)
        .set(IdentifyToStakeholdersPage, WorkForEndClient)
        //Business On Own Account Section
        .set(WorkerKnownPage,true)
        .set(MultipleContractsPage,true)
        .set(PermissionToWorkWithOthersPage,true)
        .set(OwnershipRightsPage,true)
        .set(RightsOfWorkPage,true)
        .set(TransferOfRightsPage,true)
        .set(PreviousContractPage,true)
        .set(FollowOnContractPage,true)
        .set(FirstContractPage,true)
        .set(ExtendContractPage,true)
        .set(MajorityOfWorkingTimePage,true)
        .set(SimilarWorkOtherClientsPage,true)



      lazy val request = DataRequest(fakeRequest, "id", userAnswers)

      object CheckYourAnswersHelper extends CheckYourAnswersHelper(userAnswers)

      "Return all the expected AnswerSections" in {

        val actual = CheckYourAnswersService.sections(request, implicitly)

        val expected = Seq(
          AnswerSection(
            section = Section.setup,
            headingKey = "checkYourAnswers.setup.header",
            rows = Seq(
              CheckYourAnswersHelper.whatDoYouWantToFindOut.map(_ -> None),
              CheckYourAnswersHelper.whoAreYou.map(_ -> None),
              CheckYourAnswersHelper.whatDoYouWantToDo.map(_ -> None),
              CheckYourAnswersHelper.workerUsingIntermediary.map(_ -> None),
              CheckYourAnswersHelper.contractStarted.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            section = Section.earlyExit,
            headingKey = "checkYourAnswers.exit.header",
            rows = Seq(CheckYourAnswersHelper.officeHolder.map(_ -> None)).flatten
          ),
          AnswerSection(
            section = Section.personalService,
            headingKey = "checkYourAnswers.personalService.header",
            rows = Seq(
              CheckYourAnswersHelper.arrangedSubstitute.map(_ -> None),
              CheckYourAnswersHelper.didPaySubstitute.map(_ -> None),
              CheckYourAnswersHelper.rejectSubstitute.map(_ -> None),
              CheckYourAnswersHelper.wouldWorkerPaySubstitute.map(_ -> None),
              CheckYourAnswersHelper.neededToPayHelper.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            section = Section.control,
            headingKey = "checkYourAnswers.control.header",
            rows = Seq(
              CheckYourAnswersHelper.moveWorker.map(_ -> None),
              CheckYourAnswersHelper.howWorkIsDone.map(_ -> None),
              CheckYourAnswersHelper.scheduleOfWorkingHours.map(_ -> None),
              CheckYourAnswersHelper.chooseWhereWork.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            section = Section.financialRisk,
            headingKey = "checkYourAnswers.financialRisk.header",
            rows = Seq(
              CheckYourAnswersHelper.equipmentExpenses.map(_ -> None),
              CheckYourAnswersHelper.vehicleExpenses.map(_ -> None),
              CheckYourAnswersHelper.materialsExpenses.map(_ -> None),
              CheckYourAnswersHelper.otherExpenses.map(_ -> None),
              CheckYourAnswersHelper.howWorkerIsPaid.map(_ -> None),
              CheckYourAnswersHelper.putRightAtOwnCost.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            section = Section.partAndParcel,
            headingKey = "checkYourAnswers.partParcel.header",
            rows = Seq(
              CheckYourAnswersHelper.benefits.map(_ -> None),
              CheckYourAnswersHelper.lineManagerDuties.map(_ -> None),
              CheckYourAnswersHelper.identifyToStakeholders.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            section = Section.businessOnOwnAccount,
            headingKey = "checkYourAnswers.businessOnOwnAccount.header",
            rows = Seq(
              CheckYourAnswersHelper.workerKnown.map(_ -> None),
              CheckYourAnswersHelper.multipleContracts.map(_ -> None),
              CheckYourAnswersHelper.permissionToWorkWithOthers.map(_ -> None),
              CheckYourAnswersHelper.ownershipRights.map(_ -> None),
              CheckYourAnswersHelper.rightsOfWork.map(_ -> None),
              CheckYourAnswersHelper.transferOfRights.map(_ -> None),
              CheckYourAnswersHelper.previousContract.map(_ -> None),
              CheckYourAnswersHelper.followOnContract.map(_ -> None),
              CheckYourAnswersHelper.firstContract.map(_ -> None),
              CheckYourAnswersHelper.extendContract.map(_ -> None),
              CheckYourAnswersHelper.majorityOfWorkingTime.map(_ -> None),
              CheckYourAnswersHelper.similarWorkOtherClients.map(_ -> None)
            ).flatten
          )
        )

        actual mustBe expected
      }
    }

    "when no questions are in the CacheMap" must {

      lazy val userAnswers: UserAnswers = UserAnswers("id")

      lazy val request = DataRequest(fakeRequest, "id", userAnswers)

      "Return no AnswerSections" in {

        val actual = CheckYourAnswersService.sections(request, implicitly)

        val expected = Seq(
          AnswerSection(
            section = Section.setup,
            headingKey = "checkYourAnswers.setup.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.earlyExit,
            headingKey = "checkYourAnswers.exit.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.personalService,
            headingKey = "checkYourAnswers.personalService.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.control,
            headingKey = "checkYourAnswers.control.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.financialRisk,
            headingKey = "checkYourAnswers.financialRisk.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.partAndParcel,
            headingKey = "checkYourAnswers.partParcel.header",
            rows = Seq()
          ),
          AnswerSection(
            section = Section.businessOnOwnAccount,
            headingKey = "checkYourAnswers.businessOnOwnAccount.header",
            rows = Seq()
          )
        )

        actual mustBe expected
      }
    }
  }
}
