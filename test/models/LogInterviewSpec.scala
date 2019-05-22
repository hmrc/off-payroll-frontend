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

package models

import java.time.LocalDateTime

import base.SpecBase
import config.SessionKeys
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitute.YesClientAgreed
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.{WorkerAgreeWithOthers, WorkerChooses}
import models.HowWorkIsDone.{NoWorkerInputAllowed, WorkerFollowStrictEmployeeProcedures}
import models.HowWorkerIsPaid.{Commission, HourlyDailyOrWeekly}
import models.IdentifyToStakeholders.{WorkAsBusiness, WorkAsIndependent}
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.{CannotBeCorrected, OutsideOfHoursNoCosts}
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.UserType.Hirer
import models.WorkerType.SoleTrader
import models.logging._
import models.requests.DataRequest
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.libs.json.Json
import utils.{DateTimeUtil, MockDateTimeUtil}

class LogInterviewSpec extends SpecBase {

  private val decisionResponseString =
    s"""
       |{
       |  "version": "1.0.0-beta",
       |  "correlationID": "12345",
       |  "score": {
       |    "personalService": "${WeightedAnswerEnum.HIGH}",
       |    "control": "${WeightedAnswerEnum.LOW}",
       |    "financialRisk": "${WeightedAnswerEnum.LOW}",
       |    "partAndParcel": "${WeightedAnswerEnum.LOW}"
       |  },
       |  "result": "${ResultEnum.UNKNOWN}"
       |}
    """.stripMargin

  val decisionResponse = Json.parse(decisionResponseString).as[DecisionResponse]

  "LogInterview" must {

    "construct correctly from a UserAnswers model" when {

      "all values are supplied" in {

        val userAnswers = UserAnswers("id")
          .set(AboutYouPage, 0,Worker)
          .set(ContractStartedPage,1, true)
          .set(WorkerTypePage, 2,SoleTrader)
          .set(OfficeHolderPage, 3,false)
          .set(ArrangedSubstitutePage, 4,YesClientAgreed)
          .set(DidPaySubstitutePage, 5,false)
          .set(WouldWorkerPaySubstitutePage,6, true)
          .set(RejectSubstitutePage, 7,false)
          .set(NeededToPayHelperPage, 8,false)
          .set(MoveWorkerPage, 9,CanMoveWorkerWithPermission)
          .set(HowWorkIsDonePage,10, WorkerFollowStrictEmployeeProcedures)
          .set(ScheduleOfWorkingHoursPage,11, WorkerAgreeSchedule)
          .set(ChooseWhereWorkPage, 12,WorkerAgreeWithOthers)
          .set(CannotClaimAsExpensePage,13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
          .set(HowWorkerIsPaidPage, 14,Commission)
          .set(PutRightAtOwnCostPage, 15,CannotBeCorrected)
          .set(BenefitsPage, 16,false)
          .set(LineManagerDutiesPage,17, false)
          .set(InteractWithStakeholdersPage,18, false)
          .set(IdentifyToStakeholdersPage, 19,WorkAsIndependent)

        implicit val dataRequest = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(UserType.Worker).toString), "id", userAnswers)
        val decisionRequest = Interview(userAnswers)

        val expected = LogInterview(
          decisionResponse.version,
          "",
          "ESI",
          decisionResponse.result.toString,
          None,
          Setup(
            decisionRequest.endUserRole,
            decisionRequest.hasContractStarted,
            decisionRequest.provideServices
          ),
          Exit(decisionRequest.officeHolder),
          PersonalService(
            decisionRequest.workerSentActualSubstitute,
            decisionRequest.workerPayActualSubstitute,
            decisionRequest.possibleSubstituteRejection,
            decisionRequest.possibleSubstituteWorkerPay,
            decisionRequest.wouldWorkerPayHelper
          ),
          Control(
            decisionRequest.engagerMovingWorker,
            decisionRequest.workerDecidingHowWorkIsDone,
            decisionRequest.whenWorkHasToBeDone,
            decisionRequest.workerDecideWhere
          ),
          FinancialRisk(
            decisionRequest.workerProvidedMaterials,
            decisionRequest.workerProvidedEquipment,
            decisionRequest.workerUsedVehicle,
            decisionRequest.workerHadOtherExpenses,
            decisionRequest.expensesAreNotRelevantForRole,
            decisionRequest.workerMainIncome,
            decisionRequest.paidForSubstandardWork
          ),
          PartAndParcel(
            decisionRequest.workerReceivesBenefits,
            decisionRequest.workerAsLineManager,
            decisionRequest.contactWithEngagerCustomer,
            decisionRequest.workerRepresentsEngagerBusiness
          ),
          MockDateTimeUtil.utc
        )

        val actual = LogInterview(decisionRequest, decisionResponse, MockDateTimeUtil)

        actual mustBe expected

      }

      "minimum values are supplied" in {

        val userAnswers = UserAnswers("id")

        implicit val dataRequest = DataRequest(fakeRequest.withSession(SessionKeys.userType -> Json.toJson(UserType.Worker).toString), "id", userAnswers)
        val decisionRequest = Interview(userAnswers)

        val expected = LogInterview(
          decisionResponse.version,
          "",
          "ESI",
          decisionResponse.result.toString,
          None,
          Setup(
            decisionRequest.endUserRole,
            decisionRequest.hasContractStarted,
            decisionRequest.provideServices
          ),
          Exit(decisionRequest.officeHolder),
          PersonalService(
            decisionRequest.workerSentActualSubstitute,
            decisionRequest.workerPayActualSubstitute,
            decisionRequest.possibleSubstituteRejection,
            decisionRequest.possibleSubstituteWorkerPay,
            decisionRequest.wouldWorkerPayHelper
          ),
          Control(
            decisionRequest.engagerMovingWorker,
            decisionRequest.workerDecidingHowWorkIsDone,
            decisionRequest.whenWorkHasToBeDone,
            decisionRequest.workerDecideWhere
          ),
          FinancialRisk(
            decisionRequest.workerProvidedMaterials,
            decisionRequest.workerProvidedEquipment,
            decisionRequest.workerUsedVehicle,
            decisionRequest.workerHadOtherExpenses,
            decisionRequest.expensesAreNotRelevantForRole,
            decisionRequest.workerMainIncome,
            decisionRequest.paidForSubstandardWork
          ),
          PartAndParcel(
            decisionRequest.workerReceivesBenefits,
            decisionRequest.workerAsLineManager,
            decisionRequest.contactWithEngagerCustomer,
            decisionRequest.workerRepresentsEngagerBusiness
          ),
          MockDateTimeUtil.utc
        )

        val actual = LogInterview(decisionRequest, decisionResponse, MockDateTimeUtil)

        actual mustBe expected

      }
    }

    "serialise to JSON correctly" when {

      "the maximum model is supplied" in {

        val model = LogInterview(
          version = "1.5.0-final",
          compressedInterview = "",
          route = "ESI",
          "OUT",
          None,
          setup = Setup(
            Some(Hirer),
            Some(true),
            Some(SoleTrader)
          ),
          exit = Exit(
            Some(false)
          ),
          personalService = PersonalService(
            Some(YesClientAgreed),
            Some(false),
            Some(true),
            Some(true),
            Some(false)
          ),
          control = Control(
            Some(CanMoveWorkerWithPermission),
            Some(NoWorkerInputAllowed),
            Some(WorkerAgreeSchedule),
            Some(WorkerChooses)
          ),
          financialRisk = FinancialRisk(
            Some(true),
            Some(false),
            Some(true),
            Some(false),
            Some(false),
            Some(HourlyDailyOrWeekly),
            Some(OutsideOfHoursNoCosts)
          ),
          partAndParcel = PartAndParcel(
            Some(false),
            Some(false),
            Some(false),
            Some(WorkAsBusiness)
          ),
          MockDateTimeUtil.utc
        )

        val expected = Json.obj(
          "version"-> "1.5.0-final",
          "correlationID"-> "id",
          "interview"-> Json.obj(
            "setup"-> Json.obj(
              "endUserRole"-> "personDoingWork",
              "hasContractStarted"-> "Yes",
              "provideServices"-> "soleTrader"
            ),
            "exit"-> Json.obj(
              "officeHolder"-> "No"
            ),
            "personalService"-> Json.obj(
              "workerSentActualSubstitute"-> "yesClientAgreed",
              "workerPayActualSubstitute"-> "No",
              "possibleSubstituteRejection"-> "wouldNotReject",
              "possibleSubstituteWorkerPay"-> "Yes",
              "wouldWorkerPayHelper"-> "No"
            ),
            "control"-> Json.obj(
              "engagerMovingWorker"-> "canMoveWorkerWithPermission",
              "workerDecidingHowWorkIsDone"-> "workerFollowStrictEmployeeProcedures",
              "whenWorkHasToBeDone"-> "workerAgreeSchedule",
              "workerDecideWhere"-> "workerAgreeWithOthers"
            ),
            "financialRisk"-> Json.obj(
              "workerProvidedMaterials"-> "No",
              "workerProvidedEquipment"-> "No",
              "workerUsedVehicle"-> "Yes",
              "workerHadOtherExpenses"-> "Yes",
              "expensesAreNotRelevantForRole"-> "No",
              "workerMainIncome"-> "incomeCommission",
              "paidForSubstandardWork"-> "cannotBeCorrected"
            ),
            "partAndParcel"-> Json.obj(
              "workerReceivesBenefits"-> "No",
              "workerAsLineManager"-> "No",
              "contactWithEngagerCustomer"-> "No",
              "workerRepresentsEngagerBusiness"-> "workAsIndependent"
            )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

//      "the minimum model is supplied" in {
//
//        val model = Interview("id")
//
//        val expected = Json.obj(
//          "version"-> "1.5.0-final",
//          "correlationID"-> "id",
//          "interview"-> Json.obj(
//            "setup"-> Json.obj(),
//            "exit"-> Json.obj(),
//            "personalService"-> Json.obj(),
//            "control"-> Json.obj(),
//            "financialRisk"-> Json.obj(),
//            "partAndParcel"-> Json.obj()
//          )
//        )
//
//        val actual = Json.toJson(model)
//
//        actual mustBe expected
//      }
    }
  }
}
