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
import config.featureSwitch.OptimisedFlow
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
import models.WorkerType.{LimitedCompany, SoleTrader}
import models.requests.DataRequest
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WorkerTypePage}
import play.api.libs.json.{Json, Writes}

class InterviewSpec extends GuiceAppSpecBase {

  "Interview" must {

    "find the route" when {
      "provideServices is supplied" in {

        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "ESI"

        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(LimitedCompany),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "IR35"
      }
      "use the optimised flow is both are provided" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          provideServices = Some(LimitedCompany),
          isUsingIntermediary = Some(true),
          hasContractStarted = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "IR35"
      }
      "default to IR35 when no values are supplied" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "IR35"

      }
      "isUsingIntermediary is supplied" in {

        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          isUsingIntermediary = Some(false),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "ESI"

        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          isUsingIntermediary = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).route mustBe "IR35"
      }
    }

    "calculate provide services" when {

      "provide services is populated" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).calculateProvideServices mustBe Some(SoleTrader)
      }

      "isUsingIntermediary is populated" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          isUsingIntermediary = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).calculateProvideServices mustBe Some(LimitedCompany)
      }

      "isUsingIntermediary is false" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          isUsingIntermediary = Some(false),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).calculateProvideServices mustBe Some(SoleTrader)
      }

      "use the optimised is both are supplied" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          isUsingIntermediary = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).calculateProvideServices mustBe Some(LimitedCompany)
      }

      "none is supplied" in {
        Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        ).calculateProvideServices mustBe None
      }
    }

    "construct correctly from a UserAnswers model" when {

      "OptimisedFlow is enabled" when {

        "all values are supplied" in {

          enable(OptimisedFlow)

          val userAnswers = UserAnswers("id")
            .set(AboutYouPage, 0, Worker)
            .set(ContractStartedPage, 1, true)
            .set(WorkerTypePage, 2, SoleTrader)
            .set(OfficeHolderPage, 3, false)
            .set(ArrangedSubstitutePage, 4, YesClientAgreed)
            .set(DidPaySubstitutePage, 5, false)
            .set(WouldWorkerPaySubstitutePage, 6, true)
            .set(RejectSubstitutePage, 7, false)
            .set(NeededToPayHelperPage, 8, false)
            .set(MoveWorkerPage, 9, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, 10, WorkerFollowStrictEmployeeProcedures)
            .set(ScheduleOfWorkingHoursPage, 11, WorkerAgreeSchedule)
            .set(ChooseWhereWorkPage, 12, WorkerAgreeWithOthers)
            .set(MaterialsPage, 13, false)
            .set(EquipmentExpensesPage, 14, false)
            .set(VehiclePage, 15, true)
            .set(OtherExpensesPage, 16, true)
            .set(HowWorkerIsPaidPage, 17, Commission)
            .set(PutRightAtOwnCostPage, 18, CannotBeCorrected)
            .set(BenefitsPage, 19, false)
            .set(LineManagerDutiesPage, 20, false)
            .set(IdentifyToStakeholdersPage, 22, WorkAsIndependent)

          val expected = Interview(
            correlationId = "id",
            endUserRole = Some(UserType.Worker),
            hasContractStarted = Some(true),
            provideServices = Some(SoleTrader),
            officeHolder = Some(false),
            workerSentActualSubstitute = Some(YesClientAgreed),
            workerPayActualSubstitute = Some(false),
            possibleSubstituteRejection = Some(false),
            possibleSubstituteWorkerPay = Some(true),
            wouldWorkerPayHelper = Some(false),
            engagerMovingWorker = Some(CanMoveWorkerWithPermission),
            workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
            whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
            workerDecideWhere = Some(WorkerAgreeWithOthers),
            workerProvidedMaterials = Some(false),
            workerProvidedEquipment = Some(false),
            workerUsedVehicle = Some(true),
            workerHadOtherExpenses = Some(true),
            expensesAreNotRelevantForRole = Some(false),
            workerMainIncome = Some(Commission),
            paidForSubstandardWork = Some(CannotBeCorrected),
            workerReceivesBenefits = Some(false),
            workerAsLineManager = Some(false),
            contactWithEngagerCustomer = Some(true),
            workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
          )

          val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)

          actual mustBe expected

        }

        "all expenses pages are answered no" in {

          enable(OptimisedFlow)

          val userAnswers = UserAnswers("id")
            .set(AboutYouPage, 0, Worker)
            .set(ContractStartedPage, 1, true)
            .set(WorkerTypePage, 2, SoleTrader)
            .set(OfficeHolderPage, 3, false)
            .set(ArrangedSubstitutePage, 4, YesClientAgreed)
            .set(DidPaySubstitutePage, 5, false)
            .set(WouldWorkerPaySubstitutePage, 6, true)
            .set(RejectSubstitutePage, 7, false)
            .set(NeededToPayHelperPage, 8, false)
            .set(MoveWorkerPage, 9, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, 10, WorkerFollowStrictEmployeeProcedures)
            .set(ScheduleOfWorkingHoursPage, 11, WorkerAgreeSchedule)
            .set(ChooseWhereWorkPage, 12, WorkerAgreeWithOthers)
            .set(MaterialsPage, 13, false)
            .set(EquipmentExpensesPage, 14, false)
            .set(VehiclePage, 15, false)
            .set(OtherExpensesPage, 16, false)
            .set(HowWorkerIsPaidPage, 17, Commission)
            .set(PutRightAtOwnCostPage, 18, CannotBeCorrected)
            .set(BenefitsPage, 19, false)
            .set(LineManagerDutiesPage, 20, false)
            .set(IdentifyToStakeholdersPage, 22, WorkAsIndependent)

          val expected = Interview(
            correlationId = "id",
            endUserRole = Some(UserType.Worker),
            hasContractStarted = Some(true),
            provideServices = Some(SoleTrader),
            officeHolder = Some(false),
            workerSentActualSubstitute = Some(YesClientAgreed),
            workerPayActualSubstitute = Some(false),
            possibleSubstituteRejection = Some(false),
            possibleSubstituteWorkerPay = Some(true),
            wouldWorkerPayHelper = Some(false),
            engagerMovingWorker = Some(CanMoveWorkerWithPermission),
            workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
            whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
            workerDecideWhere = Some(WorkerAgreeWithOthers),
            workerProvidedMaterials = Some(false),
            workerProvidedEquipment = Some(false),
            workerUsedVehicle = Some(false),
            workerHadOtherExpenses = Some(false),
            expensesAreNotRelevantForRole = Some(true),
            workerMainIncome = Some(Commission),
            paidForSubstandardWork = Some(CannotBeCorrected),
            workerReceivesBenefits = Some(false),
            workerAsLineManager = Some(false),
            contactWithEngagerCustomer = Some(true),
            workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
          )

          val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)

          actual mustBe expected

        }
      }

      "OptimisedFlow is disabled" when {

        "all values are supplied" in {

          val userAnswers = UserAnswers("id")
            .set(AboutYouPage, 0, Worker)
            .set(ContractStartedPage, 1, true)
            .set(WorkerTypePage, 2, SoleTrader)
            .set(OfficeHolderPage, 3, false)
            .set(ArrangedSubstitutePage, 4, YesClientAgreed)
            .set(DidPaySubstitutePage, 5, false)
            .set(WouldWorkerPaySubstitutePage, 6, true)
            .set(RejectSubstitutePage, 7, false)
            .set(NeededToPayHelperPage, 8, false)
            .set(MoveWorkerPage, 9, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, 10, WorkerFollowStrictEmployeeProcedures)
            .set(ScheduleOfWorkingHoursPage, 11, WorkerAgreeSchedule)
            .set(ChooseWhereWorkPage, 12, WorkerAgreeWithOthers)
            .set(CannotClaimAsExpensePage, 13, Seq(WorkerUsedVehicle, WorkerHadOtherExpenses))
            .set(HowWorkerIsPaidPage, 14, Commission)
            .set(PutRightAtOwnCostPage, 15, CannotBeCorrected)
            .set(BenefitsPage, 16, false)
            .set(LineManagerDutiesPage, 17, false)
            .set(InteractWithStakeholdersPage, 18, false)
            .set(IdentifyToStakeholdersPage, 19, WorkAsIndependent)

          val expected = Interview(
            correlationId = "id",
            endUserRole = Some(UserType.Worker),
            hasContractStarted = Some(true),
            provideServices = Some(SoleTrader),
            officeHolder = Some(false),
            workerSentActualSubstitute = Some(YesClientAgreed),
            workerPayActualSubstitute = Some(false),
            possibleSubstituteRejection = Some(false),
            possibleSubstituteWorkerPay = Some(true),
            wouldWorkerPayHelper = Some(false),
            engagerMovingWorker = Some(CanMoveWorkerWithPermission),
            workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
            whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
            workerDecideWhere = Some(WorkerAgreeWithOthers),
            workerProvidedMaterials = Some(false),
            workerProvidedEquipment = Some(false),
            workerUsedVehicle = Some(true),
            workerHadOtherExpenses = Some(true),
            expensesAreNotRelevantForRole = Some(false),
            workerMainIncome = Some(Commission),
            paidForSubstandardWork = Some(CannotBeCorrected),
            workerReceivesBenefits = Some(false),
            workerAsLineManager = Some(false),
            contactWithEngagerCustomer = Some(false),
            workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
          )

          val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)

          actual mustBe expected

        }
      }

      "minimum values are supplied" in {

        val userAnswers = UserAnswers("id")

        val expected = Interview("id")

        val dataRequest = DataRequest(fakeRequest, "id", userAnswers)

        val actual = Interview(userAnswers)(frontendAppConfig, dataRequest)

        actual mustBe expected

      }
    }

    "serialise to JSON correctly" when {

      "the maximum model is supplied with the new writes" in {

        implicit val writes: Writes[Interview] = NewInterview.writes

        val model = Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        )

        val expected = Json.obj(
          "version" -> "1.5.0-final",
          "correlationID" -> "id",
          "interview" -> Json.obj(
            "setup" -> Json.obj(
              "endUserRole" -> "personDoingWork",
              "hasContractStarted" -> true,
              "provideServices" -> "soleTrader"
            ),
            "exit" -> Json.obj(
              "officeHolder" -> false
            ),
            "personalService" -> Json.obj(
              "workerSentActualSubstitute" -> "yesClientAgreed",
              "workerPayActualSubstitute" -> false,
              "possibleSubstituteRejection" -> "wouldNotReject",
              "possibleSubstituteWorkerPay" -> true,
              "wouldWorkerPayHelper" -> false
            ),
            "control" -> Json.obj(
              "engagerMovingWorker" -> "canMoveWorkerWithPermission",
              "workerDecidingHowWorkIsDone" -> "workerFollowStrictEmployeeProcedures",
              "whenWorkHasToBeDone" -> "workerAgreeSchedule",
              "workerDecideWhere" -> "workerAgreeWithOthers"
            ),
            "financialRisk" -> Json.obj(
              "workerProvidedMaterials" -> false,
              "workerProvidedEquipment" -> false,
              "workerUsedVehicle" -> true,
              "workerHadOtherExpenses" -> true,
              "expensesAreNotRelevantForRole" -> false,
              "workerMainIncome" -> "incomeCommission",
              "paidForSubstandardWork" -> "cannotBeCorrected"
            ),
            "partAndParcel" -> Json.obj(
              "workerReceivesBenefits" -> false,
              "workerAsLineManager" -> false,
              "contactWithEngagerCustomer" -> false,
              "workerRepresentsEngagerBusiness" -> "workAsIndependent"
            )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

      "the maximum model is supplied" in {

        val model = Interview(
          correlationId = "id",
          endUserRole = Some(UserType.Worker),
          hasContractStarted = Some(true),
          provideServices = Some(SoleTrader),
          officeHolder = Some(false),
          workerSentActualSubstitute = Some(YesClientAgreed),
          workerPayActualSubstitute = Some(false),
          possibleSubstituteRejection = Some(false),
          possibleSubstituteWorkerPay = Some(true),
          wouldWorkerPayHelper = Some(false),
          engagerMovingWorker = Some(CanMoveWorkerWithPermission),
          workerDecidingHowWorkIsDone = Some(WorkerFollowStrictEmployeeProcedures),
          whenWorkHasToBeDone = Some(WorkerAgreeSchedule),
          workerDecideWhere = Some(WorkerAgreeWithOthers),
          workerProvidedMaterials = Some(false),
          workerProvidedEquipment = Some(false),
          workerUsedVehicle = Some(true),
          workerHadOtherExpenses = Some(true),
          expensesAreNotRelevantForRole = Some(false),
          workerMainIncome = Some(Commission),
          paidForSubstandardWork = Some(CannotBeCorrected),
          workerReceivesBenefits = Some(false),
          workerAsLineManager = Some(false),
          contactWithEngagerCustomer = Some(false),
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent)
        )

        val expected = Json.obj(
          "version" -> "1.5.0-final",
          "correlationID" -> "id",
          "interview" -> Json.obj(
            "setup" -> Json.obj(
              "endUserRole" -> "personDoingWork",
              "hasContractStarted" -> "Yes",
              "provideServices" -> "soleTrader"
            ),
            "exit" -> Json.obj(
              "officeHolder" -> "No"
            ),
            "personalService" -> Json.obj(
              "workerSentActualSubstitute" -> "yesClientAgreed",
              "workerPayActualSubstitute" -> "No",
              "possibleSubstituteRejection" -> "wouldNotReject",
              "possibleSubstituteWorkerPay" -> "Yes",
              "wouldWorkerPayHelper" -> "No"
            ),
            "control" -> Json.obj(
              "engagerMovingWorker" -> "canMoveWorkerWithPermission",
              "workerDecidingHowWorkIsDone" -> "workerFollowStrictEmployeeProcedures",
              "whenWorkHasToBeDone" -> "workerAgreeSchedule",
              "workerDecideWhere" -> "workerAgreeWithOthers"
            ),
            "financialRisk" -> Json.obj(
              "workerProvidedMaterials" -> "No",
              "workerProvidedEquipment" -> "No",
              "workerUsedVehicle" -> "Yes",
              "workerHadOtherExpenses" -> "Yes",
              "expensesAreNotRelevantForRole" -> "No",
              "workerMainIncome" -> "incomeCommission",
              "paidForSubstandardWork" -> "cannotBeCorrected"
            ),
            "partAndParcel" -> Json.obj(
              "workerReceivesBenefits" -> "No",
              "workerAsLineManager" -> "No",
              "contactWithEngagerCustomer" -> "No",
              "workerRepresentsEngagerBusiness" -> "workAsIndependent"
            )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

      "the minimum model is supplied" in {

        val model = Interview("id")

        val expected = Json.obj(
          "version" -> "1.5.0-final",
          "correlationID" -> "id",
          "interview" -> Json.obj(
            "setup" -> Json.obj(),
            "exit" -> Json.obj(),
            "personalService" -> Json.obj(),
            "control" -> Json.obj(),
            "financialRisk" -> Json.obj(),
            "partAndParcel" -> Json.obj()
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }
    }
  }
}
