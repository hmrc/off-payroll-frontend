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

import models.requests.DataRequest
import models.sections.businessOnOwnAccount.ExclusiveContract.{AbleToProvideServices, UnableToProvideServices, _}
import models.sections.businessOnOwnAccount.MultipleEngagements.{NoKnowledgeOfExternalActivity, OnlyContractForPeriod, ProvidedServicesToOtherEngagers}
import models.sections.businessOnOwnAccount.SeriesOfContracts.{ContractCouldBeExtended, ContractIsPartOfASeries, StandAloneContract}
import models.sections.businessOnOwnAccount.SignificantWorkingTime.{ConsumesSignificantAmount, NoSignificantAmount}
import models.sections.businessOnOwnAccount.TransferRights.{AbleToTransferRights, NoRightsArise, RetainOwnershipRights, RightsTransferredToClient}
import models.sections.control.ChooseWhereWork.WorkerAgreeWithOthers
import models.sections.control.HowWorkIsDone.WorkerFollowStrictEmployeeProcedures
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.WorkerAgreeSchedule
import models.sections.financialRisk.CannotClaimAsExpense.{WorkerHadOtherExpenses, WorkerUsedVehicle}
import models.sections.financialRisk.HowWorkerIsPaid.Commission
import models.sections.financialRisk.PutRightAtOwnCost.CannotBeCorrected
import models.sections.partAndParcel.IdentifyToStakeholders.WorkAsIndependent
import models.sections.personalService.ArrangedSubstitute.YesClientAgreed
import models.sections.setup.AboutYouAnswer.Worker
import models.sections.setup.WorkerType.{LimitedCompany, SoleTrader}
import pages.sections.businessOnOwnAccount._
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
      "use the normal flow is both are provided" in {
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



          val userAnswers = UserAnswers("id")
            .set(AboutYouPage, Worker)
            .set(ContractStartedPage, true)
            .set(WorkerTypePage, SoleTrader)
            .set(OfficeHolderPage, false)
            .set(ArrangedSubstitutePage, YesClientAgreed)
            .set(DidPaySubstitutePage, false)
            .set(WouldWorkerPaySubstitutePage, true)
            .set(RejectSubstitutePage, false)
            .set(NeededToPayHelperPage, false)
            .set(MoveWorkerPage, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, WorkerFollowStrictEmployeeProcedures)
            .set(ScheduleOfWorkingHoursPage, WorkerAgreeSchedule)
            .set(ChooseWhereWorkPage, WorkerAgreeWithOthers)
            .set(MaterialsPage, false)
            .set(EquipmentExpensesPage, false)
            .set(VehiclePage, true)
            .set(OtherExpensesPage, true)
            .set(HowWorkerIsPaidPage, Commission)
            .set(PutRightAtOwnCostPage, CannotBeCorrected)
            .set(BenefitsPage, false)
            .set(LineManagerDutiesPage, false)
            .set(IdentifyToStakeholdersPage, WorkAsIndependent)

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



          val userAnswers = UserAnswers("id")
            .set(AboutYouPage, Worker)
            .set(ContractStartedPage, true)
            .set(WorkerTypePage, SoleTrader)
            .set(OfficeHolderPage, false)
            .set(ArrangedSubstitutePage, YesClientAgreed)
            .set(DidPaySubstitutePage, false)
            .set(WouldWorkerPaySubstitutePage, true)
            .set(RejectSubstitutePage, false)
            .set(NeededToPayHelperPage, false)
            .set(MoveWorkerPage, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, WorkerFollowStrictEmployeeProcedures)
            .set(ScheduleOfWorkingHoursPage, WorkerAgreeSchedule)
            .set(ChooseWhereWorkPage, WorkerAgreeWithOthers)
            .set(MaterialsPage, false)
            .set(EquipmentExpensesPage, false)
            .set(VehiclePage, false)
            .set(OtherExpensesPage, false)
            .set(HowWorkerIsPaidPage, Commission)
            .set(PutRightAtOwnCostPage, CannotBeCorrected)
            .set(BenefitsPage, false)
            .set(LineManagerDutiesPage, false)
            .set(IdentifyToStakeholdersPage, WorkAsIndependent)

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

        "BoOA section" when {

          "ExclusiveContract answer" when {

            "MultipleContractsPage is answered yes" must {

              "be UnableToProvideServices" in {



                val userAnswers = UserAnswers("id")
                  .set(MultipleContractsPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  exclusiveContract = Some(UnableToProvideServices)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "MultipleContractsPage is false and PermissionToWorkWithOthersPage is true" must {

              "be AbleToProvideServicesWithPermission" in {



                val userAnswers = UserAnswers("id")
                  .set(MultipleContractsPage, false)
                  .set(PermissionToWorkWithOthersPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  exclusiveContract = Some(AbleToProvideServicesWithPermission)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "MultipleContractsPage is false and PermissionToWorkWithOthersPage is false" must {

              "be AbleToProvideServices" in {



                val userAnswers = UserAnswers("id")
                  .set(MultipleContractsPage, false)
                  .set(PermissionToWorkWithOthersPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  exclusiveContract = Some(AbleToProvideServices)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }
          }

          "TransferRights answer" when {

            "OwnershipRightsPage is false" must {

              "be NoRightsArise" in {



                val userAnswers = UserAnswers("id")
                  .set(OwnershipRightsPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  transferRights = Some(NoRightsArise)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "OwnershipRightsPage is true and RightsOfWorkPage is true" must {

              "be RightsTransferredToClient" in {



                val userAnswers = UserAnswers("id")
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  transferRights = Some(RightsTransferredToClient)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "OwnershipRightsPage is true, RightsOfWorkPage is false and TransferOfRightsPage is false" must {

              "be RetainOwnershipRights" in {



                val userAnswers = UserAnswers("id")
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  transferRights = Some(RetainOwnershipRights)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "OwnershipRightsPage is true, RightsOfWorkPage is false and TransferOfRightsPage is true" must {

              "be RetainOwnershipRights" in {



                val userAnswers = UserAnswers("id")
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  transferRights = Some(AbleToTransferRights)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }
          }

          "MultipleEngagements answer" when {

            "SimilarWorkOtherClientsPage is true" must {

              "be ProvidedServicesToOtherEngagers" in {



                val userAnswers = UserAnswers("id")
                  .set(SimilarWorkOtherClientsPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  multipleEngagements = Some(ProvidedServicesToOtherEngagers)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "SimilarWorkOtherClientsPage is false" must {

              "be OnlyContractForPeriod" in {



                val userAnswers = UserAnswers("id")
                  .set(SimilarWorkOtherClientsPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  multipleEngagements = Some(OnlyContractForPeriod)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }
          }

          "SignificantWorkingTime answer" when {

            "MajorityOfWorkingTimePage is true and FinanciallyDependentPage is true" must {

              "be ConsumesSignificantAmount" in {



                val userAnswers = UserAnswers("id")
                  .set(MajorityOfWorkingTimePage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  significantWorkingTime = Some(ConsumesSignificantAmount)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "MajorityOfWorkingTimePage is false and FinanciallyDependentPage is false" must {

              "be NoSignificantAmount" in {



                val userAnswers = UserAnswers("id")
                  .set(MajorityOfWorkingTimePage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  significantWorkingTime = Some(NoSignificantAmount)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }
          }
        }

          "SeriesOfContracts answer" when {

            "PreviousContractPage is true and FollowOnContractPage is true" must {

              "be ContractIsPartOfASeries" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(ContractIsPartOfASeries)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is true and FollowOnContractPage is false and FirstInSeries is true" must {

              "be ContractIsPartOfASeries" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FollowOnContractPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(ContractIsPartOfASeries)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is true, FollowOnContract is false and FirstContractPage is false and Extended is false" must {

              "be StandAloneContract" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(StandAloneContract)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is true, FollowOnContract is false and FirstContractPage is false and Extended is true" must {

              "be ContractCouldBeExtended" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(ContractCouldBeExtended)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is false, FirstContractPage is true" must {

              "be ContractIsPartOfASeries" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, false)
                  .set(FirstContractPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(ContractIsPartOfASeries)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is false, FirstContractPage is false and ExtendContract is false" must {

              "be StandaloneContract" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, false)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(StandAloneContract)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }

            "PreviousContractPage is false, FirstContractPage is false and ExtendContract is true" must {

              "be StandaloneContract" in {



                val userAnswers = UserAnswers("id")
                  .set(PreviousContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)

                val expected = Interview(
                  correlationId = "id",
                  endUserRole = Some(UserType.Worker),
                  seriesOfContracts = Some(ContractCouldBeExtended)
                )

                val actual = Interview(userAnswers)(frontendAppConfig, workerFakeDataRequest)
                actual mustBe expected
              }
            }
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

      "the maximum model is supplied with the writes" in {

        implicit val writes: Writes[Interview] = Interview.writes

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
          "version" -> frontendAppConfig.decisionVersion,
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
            ),
            "businessOnOwnAccount" -> Json.obj(

            )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }


      "the maximum model is supplied with writes" in {

        implicit val writes: Writes[Interview] = Interview.writes

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
          workerRepresentsEngagerBusiness = Some(WorkAsIndependent),
          exclusiveContract = Some(AbleToProvideServices),
          transferRights = Some(AbleToTransferRights),
          multipleEngagements = Some(NoKnowledgeOfExternalActivity),
          significantWorkingTime = Some(ConsumesSignificantAmount),
          seriesOfContracts = Some(ContractCouldBeExtended)
        )

        val expected = Json.obj(
          "version" -> frontendAppConfig.decisionVersion,
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
            ),
            "businessOnOwnAccount" -> Json.obj(
              "exclusiveContract" -> "ableToProvideServices",
              "transferRights" -> "ableToTransferRights",
              "multipleEngagements" -> "noKnowledgeOfExternalActivity",
              "significantWorkingTime" -> "consumesSignificantAmount",
              "seriesOfContracts" -> "contractCouldBeExtended"
            )
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

      "the minimum model is supplied" in {

        implicit val writes: Writes[Interview] = Interview.writes

        val model = Interview("id")

        val expected = Json.obj(
          "version" -> frontendAppConfig.decisionVersion,
          "correlationID" -> "id",
          "interview" -> Json.obj(
            "setup" -> Json.obj(),
            "exit" -> Json.obj(),
            "personalService" -> Json.obj(),
            "control" -> Json.obj(),
            "financialRisk" -> Json.obj(),
            "partAndParcel" -> Json.obj(),
            "businessOnOwnAccount" -> Json.obj()
          )
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }
    }
  }
}
