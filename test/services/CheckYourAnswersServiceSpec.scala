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

import base.SpecBase
import models.ArrangedSubstitute.YesClientAgreed
import models.BusinessSize.Turnover
import models.CannotClaimAsExpense.WorkerProvidedMaterials
import models.ChooseWhereWork.WorkerChooses
import models.HowWorkIsDone.NoWorkerInputAllowed
import models.HowWorkerIsPaid.HourlyDailyOrWeekly
import models.IdentifyToStakeholders.WorkForEndClient
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.OutsideOfHoursNoCosts
import models.ScheduleOfWorkingHours.ScheduleDecidedForWorker
import models.WhichDescribesYouAnswer.WorkerPAYE
import models._
import models.requests.DataRequest
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import utils.CheckYourAnswersHelper
import viewmodels.AnswerSection

class CheckYourAnswersServiceSpec extends SpecBase {

  object CheckYourAnswersService extends CheckYourAnswersService

  "CheckYourAnswersService.sections" must {

    "when all questions are in the CacheMap" must {

      lazy val userAnswers: UserAnswers = UserAnswers("id")
        //Setup Section
        .set(WhichDescribesYouPage,0, WorkerPAYE)
        .set(WorkerUsingIntermediaryPage,1, true)
        .set(IsWorkForPrivateSectorPage,2, true)
        .set(BusinessSizePage,3, Seq(Turnover))
        .set(ContractStartedPage, 4, true)
        //Exit Section
        .set(OfficeHolderPage, 5, true)
        //Personal Service section
        .set(ArrangedSubstitutePage, 6, YesClientAgreed)
        .set(DidPaySubstitutePage, 7, true)
        .set(RejectSubstitutePage, 8, true)
        .set(WouldWorkerPaySubstitutePage, 9, true)
        .set(NeededToPayHelperPage, 10, true)
        //Control section
        .set(ChooseWhereWorkPage, 10, WorkerChooses)
        .set(HowWorkIsDonePage, 11, NoWorkerInputAllowed)
        .set(MoveWorkerPage, 12, CanMoveWorkerWithPermission)
        .set(ScheduleOfWorkingHoursPage, 13, ScheduleDecidedForWorker)
        //Financial Risk section
        .set(CannotClaimAsExpensePage, 14, Seq(WorkerProvidedMaterials))
        .set(HowWorkerIsPaidPage, 15, HourlyDailyOrWeekly)
        .set(PutRightAtOwnCostPage, 16, OutsideOfHoursNoCosts)
        //Part and Parcel section
        .set(BenefitsPage, 17, false)
        .set(LineManagerDutiesPage, 18, false)
        .set(InteractWithStakeholdersPage, 19, true)
        .set(IdentifyToStakeholdersPage, 20, WorkForEndClient)


      lazy val request = DataRequest(fakeRequest, "id", userAnswers)

      object CheckYourAnswersHelper extends CheckYourAnswersHelper(userAnswers)

      "Return all the expected AnswerSections" in {

        val actual = CheckYourAnswersService.sections(request, implicitly)

        val expected = Seq(
          AnswerSection(
            headingKey = Some("checkYourAnswers.setup.header"),
            rows = Seq(
              CheckYourAnswersHelper.aboutYouOptimised.map(_ -> None),
              CheckYourAnswersHelper.workerTypeOptimised.map(_ -> None),
              CheckYourAnswersHelper.isWorkForPrivateSector.map(_ -> None),
              CheckYourAnswersHelper.businessSize.map(_ -> None),
              CheckYourAnswersHelper.contractStarted.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.exit.header"),
            rows = Seq(CheckYourAnswersHelper.officeHolder.map(_ -> None)).flatten
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.personalService.header"),
            rows = Seq(
              CheckYourAnswersHelper.arrangedSubstitute.map(_ -> None),
              CheckYourAnswersHelper.didPaySubstitute.map(_ -> None),
              CheckYourAnswersHelper.rejectSubstitute.map(_ -> None),
              CheckYourAnswersHelper.wouldWorkerPaySubstitute.map(_ -> None),
              CheckYourAnswersHelper.neededToPayHelper.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.control.header"),
            rows = Seq(
              CheckYourAnswersHelper.moveWorker.map(_ -> None),
              CheckYourAnswersHelper.howWorkIsDone.map(_ -> None),
              CheckYourAnswersHelper.scheduleOfWorkingHours.map(_ -> None),
              CheckYourAnswersHelper.chooseWhereWork.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.financialRisk.header"),
            rows = Seq(
              CheckYourAnswersHelper.cannotClaimAsExpenseOptimised.map(_ -> None),
              CheckYourAnswersHelper.howWorkerIsPaid.map(_ -> None),
              CheckYourAnswersHelper.putRightAtOwnCost.map(_ -> None)
            ).flatten
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.partParcel.header"),
            rows = Seq(
              CheckYourAnswersHelper.benefits.map(_ -> None),
              CheckYourAnswersHelper.lineManagerDuties.map(_ -> None),
              CheckYourAnswersHelper.interactWithStakeholders.map(_ -> None),
              CheckYourAnswersHelper.identifyToStakeholders.map(_ -> None)
            ).flatten
          )
        )

        actual mustBe expected
      }

    }

    "when no questions are in the CacheMap" must {

      lazy val userAnswers: UserAnswers = UserAnswers("id")

      lazy val request = DataRequest(fakeRequest, "id", userAnswers)

      object CheckYourAnswersHelper extends CheckYourAnswersHelper(userAnswers)

      "Return no AnswerSections" in {

        val actual = CheckYourAnswersService.sections(request, implicitly)

        val expected = Seq(
          AnswerSection(
            headingKey = Some("checkYourAnswers.setup.header"),
            rows = Seq()
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.exit.header"),
            rows = Seq()
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.personalService.header"),
            rows = Seq()
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.control.header"),
            rows = Seq()
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.financialRisk.header"),
            rows = Seq()
          ),
          AnswerSection(
            headingKey = Some("checkYourAnswers.partParcel.header"),
            rows = Seq()
          )
        )

        actual mustBe expected
      }

    }
  }
}
