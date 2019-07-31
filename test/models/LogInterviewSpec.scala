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

import base.GuiceAppSpecBase
import models.AboutYouAnswer.Worker
import models.ArrangedSubstitute.YesClientAgreed
import models.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.ChooseWhereWork.WorkerAgreeWithOthers
import models.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.HowWorkerIsPaid.Commission
import models.IdentifyToStakeholders.WorkAsIndependent
import models.MoveWorker.CanMoveWorkerWithPermission
import models.PutRightAtOwnCost.CannotBeCorrected
import models.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.WorkerType.SoleTrader
import models.logging._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.libs.json.Json
import utils.MockDateTimeUtil

class LogInterviewSpec extends GuiceAppSpecBase {

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

        val decisionRequest = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)

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

        val decisionRequest = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)

        val expected = LogInterview(
          version = decisionResponse.version,
          compressedInterview = "",
          route = "IR35",
          decision = decisionResponse.result.toString,
          count = None,
          setup = Setup(
            endUserRole = decisionRequest.endUserRole,
            hasContractStarted = decisionRequest.hasContractStarted,
            provideServices = decisionRequest.provideServices
          ),
          exit = Exit(decisionRequest.officeHolder),
          personalService = PersonalService(
            workerSentActualSubstitute = decisionRequest.workerSentActualSubstitute,
            workerPayActualSubstitute = decisionRequest.workerPayActualSubstitute,
            possibleSubstituteRejection = decisionRequest.possibleSubstituteRejection,
            possibleSubstituteWorkerPay = decisionRequest.possibleSubstituteWorkerPay,
            wouldWorkerPayHelper = decisionRequest.wouldWorkerPayHelper
          ),
          control = Control(
            engagerMovingWorker = decisionRequest.engagerMovingWorker,
            workerDecidingHowWorkIsDone = decisionRequest.workerDecidingHowWorkIsDone,
            workHasToBeDone = decisionRequest.whenWorkHasToBeDone,
            workerDecideWhere = decisionRequest.workerDecideWhere
          ),
          financialRisk = FinancialRisk(
            workerProvidedMaterials = decisionRequest.workerProvidedMaterials,
            workerProvidedEquipment = decisionRequest.workerProvidedEquipment,
            workerUsedVehicle = decisionRequest.workerUsedVehicle,
            workerHadOtherExpenses = decisionRequest.workerHadOtherExpenses,
            expensesAreNotRelevantForRole = decisionRequest.expensesAreNotRelevantForRole,
            workerMainIncome = decisionRequest.workerMainIncome,
            paidForSubstandardWork = decisionRequest.paidForSubstandardWork
          ),
          partAndParcel = PartAndParcel(
            workerReceivesBenefits = decisionRequest.workerReceivesBenefits,
            workerAsLineManager = decisionRequest.workerAsLineManager,
            contactWithEngagerCustomer = decisionRequest.contactWithEngagerCustomer,
            workerRepresentsEngagerBusiness = decisionRequest.workerRepresentsEngagerBusiness
          ),
          completed = MockDateTimeUtil.utc
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
          decision = "OUT",
          count = None,
          setup = Setup(
            endUserRole = Some(UserType.Worker),
            hasContractStarted = Some(true),
            provideServices = Some(SoleTrader)
          ),
          exit = Exit(
            officeHolder = Some(false)
          ),
          personalService = PersonalService(
            workerSentActualSubstitute = Some(YesClientAgreed),
            workerPayActualSubstitute = Some(false),
            possibleSubstituteRejection = Some(true),
            possibleSubstituteWorkerPay = Some(true),
            wouldWorkerPayHelper = Some(false)
          ),
          control = Control(
            engagerMovingWorker = Some(CanMoveWorkerWithPermission),
            workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
            workHasToBeDone = Some(WorkerAgreeSchedule),
            workerDecideWhere = Some(WorkerAgreeWithOthers)
          ),
          financialRisk = FinancialRisk(
            workerProvidedMaterials = Some(false),
            workerProvidedEquipment = Some(false),
            workerUsedVehicle = Some(true),
            workerHadOtherExpenses = Some(true),
            expensesAreNotRelevantForRole = Some(false),
            workerMainIncome = Some(Commission),
            paidForSubstandardWork = Some(CannotBeCorrected)
          ),
          partAndParcel = PartAndParcel(
            workerReceivesBenefits = Some(false),
            workerAsLineManager = Some(false),
            contactWithEngagerCustomer = Some(false),
            workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
          ),
          completed = MockDateTimeUtil.utc
        )

        val expected = Json.obj(
          "version"-> "1.5.0-final",
          "compressedInterview"-> "",
          "route"-> "ESI",
          "decision"-> "OUT",
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
            "possibleSubstituteRejection"-> "Yes",
            "possibleSubstituteWorkerPay"-> "Yes",
            "wouldWorkerPayHelper"-> "No"
          ),
          "control"-> Json.obj(
            "engagerMovingWorker"-> "canMoveWorkerWithPermission",
            "workerDecidingHowWorkIsDone"-> "workerFollowStrictEmployeeProcedures",
            "workHasToBeDone"-> "workerAgreeSchedule",
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
          ),
          "completed" -> "2019-05-22 10:15:30"
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

      "the minimum model is supplied" in {

        val model = LogInterview(
          version = "1.5.0-final",
          compressedInterview = "",
          route = "ESI",
          decision = "OUT",
          count = None,
          setup = Setup(
            endUserRole = None,
            hasContractStarted = None,
            provideServices = None
          ),
          exit = Exit(
            officeHolder = None
          ),
          personalService = PersonalService(
            workerSentActualSubstitute = None,
            workerPayActualSubstitute = None,
            possibleSubstituteRejection = None,
            possibleSubstituteWorkerPay = None,
            wouldWorkerPayHelper = None
          ),
          control = Control(
            engagerMovingWorker = None,
            workerDecidingHowWorkIsDone = None,
            workHasToBeDone = None,
            workerDecideWhere = None
          ),
          financialRisk = FinancialRisk(
            workerProvidedMaterials = None,
            workerProvidedEquipment = None,
            workerUsedVehicle = None,
            workerHadOtherExpenses = None,
            expensesAreNotRelevantForRole = None,
            workerMainIncome = None,
            paidForSubstandardWork = None
          ),
          partAndParcel = PartAndParcel(
            workerReceivesBenefits = None,
            workerAsLineManager = None,
            contactWithEngagerCustomer = None,
            workerRepresentsEngagerBusiness = None
          ),
          completed = MockDateTimeUtil.utc
        )

        val expected = Json.obj(
          "version"-> "1.5.0-final",
          "compressedInterview"-> "",
          "route"-> "ESI",
          "decision"-> "OUT",
          "setup"-> Json.obj(),
          "exit"-> Json.obj(),
          "personalService"-> Json.obj(),
          "control"-> Json.obj(),
          "financialRisk"-> Json.obj(),
          "partAndParcel"-> Json.obj(),
          "completed" -> "2019-05-22 10:15:30"
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }
    }
  }
}
