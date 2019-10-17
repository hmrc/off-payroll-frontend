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
import models.sections.control.ChooseWhereWork.WorkerCannotChoose
import models.sections.control.HowWorkIsDone.NoWorkerInputAllowed
import models.sections.control.MoveWorker.CanMoveWorkerWithPermission
import models.sections.control.ScheduleOfWorkingHours.WorkerDecideSchedule
import models.sections.financialRisk.HowWorkerIsPaid.PieceRate
import models.sections.financialRisk.PutRightAtOwnCost.AsPartOfUsualRateInWorkingHours
import models.sections.partAndParcel.IdentifyToStakeholders.WorkForEndClient
import models.sections.personalService.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models.sections.setup.WhatDoYouWantToDo.MakeNewDetermination
import models.sections.setup.WhoAreYou.{Client, Worker}
import models.sections.setup.{WhatDoYouWantToFindOut, WhoAreYou}
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._

//noinspection ScalaStyle
class CheckYourAnswersValidationServiceSpec extends GuiceAppSpecBase {

  object CheckYourAnswersService extends CheckYourAnswersValidationService

  "CheckYourAnswersService.isValid" must {

    "For the error scenarios (invalid use cases)" should {

      "For the Setup Section" should {

        "return an error" when {

          "WantToFindOut, WhoAreYou and ContractStarted are not supplied" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WhatDoYouWantToFindOutPage)
            result.left.get must contain(WhoAreYouPage)
            result.left.get must contain(ContractStartedPage)
          }

          "User is Hirer and Intermediary is missed" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
              .set(WhoAreYouPage, Client)
              .set(ContractStartedPage, true)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WorkerUsingIntermediaryPage)
          }

          "User is Worker and WantToDo is missing" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
              .set(WhoAreYouPage, Worker)
              .set(ContractStartedPage, true)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WhatDoYouWantToDoPage)
          }

          "User is Worker and Making a new Determination; Intermediary is missing" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.IR35)
              .set(WhoAreYouPage, Worker)
              .set(WhatDoYouWantToDoPage, MakeNewDetermination)
              .set(ContractStartedPage, true)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WorkerUsingIntermediaryPage)
          }
        }
      }

      "For the Exit Section" should {

        "return an error" when {

          "OfficeHolder is not answered" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(OfficeHolderPage)
          }
        }
      }

      "For the Personal Service Section" should {

        "return an error" when {

          "RejectSubstitute is not answered when contract has not stared yet" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, false)
              //Early Exit
              .set(OfficeHolderPage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(RejectSubstitutePage)
          }

          "WouldWorkerPaySubstitute is not answered, contract has not started yet AND RejectSubstitute is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, false)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(RejectSubstitutePage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WouldWorkerPaySubstitutePage)
          }

          "ArrangedSubstitute is not answered when contract started" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(ArrangedSubstitutePage)
          }

          "DidWorkerPaySubstitute is not answered when contract started AND client agreed to substitution" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, YesClientAgreed)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(DidPaySubstitutePage)
          }

          "NeededToPayHelper is not answered when contract started AND client agreed to substitution AND worker did not pay substitute" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, YesClientAgreed)
              .set(DidPaySubstitutePage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(NeededToPayHelperPage)
          }

          "NeededToPayHelper is not answered when contract started AND client NOT agreed to substitution" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, YesClientNotAgreed)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(NeededToPayHelperPage)
          }

          "RejectSubstitute is not answered when contract started AND no substitution happened" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(RejectSubstitutePage)
          }

          "NeededToPayHelper is not answered when contract started AND no substitution happened AND RejectSubstitute is true" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, true)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(NeededToPayHelperPage)
          }

          "WouldWorkerPaySubstitute is not answered when contract started AND no substitution happened AND RejectSubstitute is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(WouldWorkerPaySubstitutePage)
          }

          "NeededToPayHelper is not answered when contract started AND no substitution happened AND RejectSubstitute is false AND WouldPaySubstitute is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(NeededToPayHelperPage)
          }
        }
      }

      "For the Control Section" should {

        "return an error" when {

          "any question is missing" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(MoveWorkerPage)
            result.left.get must contain(HowWorkIsDonePage)
            result.left.get must contain(ScheduleOfWorkingHoursPage)
            result.left.get must contain(ChooseWhereWorkPage)
          }
        }
      }

      "For the FinancialRisk Section" should {

        "return an error" when {

          "any question is missing" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(MaterialsPage)
            result.left.get must contain(VehiclePage)
            result.left.get must contain(OtherExpensesPage)
            result.left.get must contain(EquipmentExpensesPage)
            result.left.get must contain(HowWorkerIsPaidPage)
            result.left.get must contain(PutRightAtOwnCostPage)
          }
        }
      }

      "For the Part and Parcel Section" should {

        "return an error" when {

          "any question is missing" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)

            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(BenefitsPage)
            result.left.get must contain(LineManagerDutiesPage)
            result.left.get must contain(IdentifyToStakeholdersPage)
          }
        }
      }

      "For the Business On Own Account Section" should {

        "return an error" when {

          "the worker is known (User is Worker)" when {

            "any mandatory question is missing" in {

              lazy val userAnswers: UserAnswers = UserAnswers("id")
                //Setup Section
                .set(WhoAreYouPage, WhoAreYou.Worker)
                .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                .set(WorkerUsingIntermediaryPage, true)
                .set(ContractStartedPage, true)
                //Early Exit
                .set(OfficeHolderPage, false)
                //Personal Service Section
                .set(ArrangedSubstitutePage, No)
                .set(RejectSubstitutePage, false)
                .set(WouldWorkerPaySubstitutePage, false)
                .set(NeededToPayHelperPage, true)
                //Control Section
                .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                .set(ChooseWhereWorkPage, WorkerCannotChoose)
                //Financial Risk Section
                .set(MaterialsPage, false)
                .set(VehiclePage, true)
                .set(OtherExpensesPage, false)
                .set(EquipmentExpensesPage, true)
                .set(HowWorkerIsPaidPage, PieceRate)
                .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                //Business On Own Account

              lazy val result = CheckYourAnswersService.isValid(userAnswers)

              result.isLeft mustBe true
              result.left.get must contain(MultipleContractsPage)
              result.left.get must contain(OwnershipRightsPage)
              result.left.get must contain(PreviousContractPage)
              result.left.get must contain(MajorityOfWorkingTimePage)
              result.left.get must contain(SimilarWorkOtherClientsPage)
            }
          }

          "Permission to Work is missing when MultipleContracts is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(PermissionToWorkWithOthersPage)
          }

          "RightsOfWork is missing when OwnershipRights is true" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(RightsOfWorkPage)
          }

          "TransferRights is missing when RightsOfWork is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)
              .set(RightsOfWorkPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(TransferOfRightsPage)
          }

          "FirstContract is missing when PreviousContract is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)
              .set(RightsOfWorkPage, false)
              .set(TransferOfRightsPage, true)
              .set(PreviousContractPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(FirstContractPage)
          }

          "FollowOnContract is missing when PreviousContract is true" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)
              .set(RightsOfWorkPage, false)
              .set(TransferOfRightsPage, true)
              .set(PreviousContractPage, true)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(FollowOnContractPage)
          }

          "FirstContract is missing when PreviousContract is true and FollowOnContract is false" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)
              .set(RightsOfWorkPage, false)
              .set(TransferOfRightsPage, true)
              .set(PreviousContractPage, true)
              .set(FollowOnContractPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(FirstContractPage)
          }

          "ExtendContract is missing when FirstContract" in {

            lazy val userAnswers: UserAnswers = UserAnswers("id")
              //Setup Section
              .set(WhoAreYouPage, WhoAreYou.Worker)
              .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
              .set(WorkerUsingIntermediaryPage, true)
              .set(ContractStartedPage, true)
              //Early Exit
              .set(OfficeHolderPage, false)
              //Personal Service Section
              .set(ArrangedSubstitutePage, No)
              .set(RejectSubstitutePage, false)
              .set(WouldWorkerPaySubstitutePage, false)
              .set(NeededToPayHelperPage, true)
              //Control Section
              .set(MoveWorkerPage, CanMoveWorkerWithPermission)
              .set(HowWorkIsDonePage, NoWorkerInputAllowed)
              .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
              .set(ChooseWhereWorkPage, WorkerCannotChoose)
              //Financial Risk Section
              .set(MaterialsPage, false)
              .set(VehiclePage, true)
              .set(OtherExpensesPage, false)
              .set(EquipmentExpensesPage, true)
              .set(HowWorkerIsPaidPage, PieceRate)
              .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
              //Business On Own Account
              .set(MultipleContractsPage, true)
              .set(PermissionToWorkWithOthersPage, true)
              .set(OwnershipRightsPage, true)
              .set(RightsOfWorkPage, false)
              .set(TransferOfRightsPage, true)
              .set(PreviousContractPage, true)
              .set(FollowOnContractPage, false)
              .set(FirstContractPage, false)


            lazy val result = CheckYourAnswersService.isValid(userAnswers)

            result.isLeft mustBe true
            result.left.get must contain(ExtendContractPage)
          }
        }
      }
    }

    "For the valid scenarios (valid use cases)" should {

      "For someone who is NOT trading through an intermediary" should {

        "return Valid (Right(true))" in {

          lazy val userAnswers: UserAnswers = UserAnswers("id")
            //Setup Section
            .set(WhoAreYouPage, WhoAreYou.Worker)
            .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
            .set(WorkerUsingIntermediaryPage, false)
            .set(ContractStartedPage, true)
            //Early Exit
            .set(OfficeHolderPage, false)
            //Personal Service Section
            .set(ArrangedSubstitutePage, No)
            .set(RejectSubstitutePage, false)
            .set(WouldWorkerPaySubstitutePage, false)
            .set(NeededToPayHelperPage, true)
            //Control Section
            .set(MoveWorkerPage, CanMoveWorkerWithPermission)
            .set(HowWorkIsDonePage, NoWorkerInputAllowed)
            .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
            .set(ChooseWhereWorkPage, WorkerCannotChoose)
            //Financial Risk Section
            .set(MaterialsPage, false)
            .set(VehiclePage, true)
            .set(OtherExpensesPage, false)
            .set(EquipmentExpensesPage, true)
            .set(HowWorkerIsPaidPage, PieceRate)
            .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
            //Part and Parcel Section
            .set(BenefitsPage, true)
            .set(LineManagerDutiesPage, true)
            .set(IdentifyToStakeholdersPage, WorkForEndClient)
            //Business On Own Account
            .set(MultipleContractsPage, true)
            .set(PermissionToWorkWithOthersPage, true)
            .set(OwnershipRightsPage, true)
            .set(RightsOfWorkPage, false)
            .set(TransferOfRightsPage, true)
            .set(PreviousContractPage, true)
            .set(FollowOnContractPage, false)
            .set(FirstContractPage, false)
            .set(ExtendContractPage, true)
            .set(MajorityOfWorkingTimePage, true)
            .set(SimilarWorkOtherClientsPage, true)

          lazy val result = CheckYourAnswersService.isValid(userAnswers)

          result mustBe Right(true)
        }
      }

      "For someone who IS trading through an intermediary" should {

        "For a Public Sector journey" when {

          "Contract has Started" when {

            "Substitution has never happened" +
              "AND the client would not reject a substitute" +
              "AND the worker would not pay the substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, No)
                  .set(RejectSubstitutePage, false)
                  .set(WouldWorkerPaySubstitutePage, false)
                  .set(NeededToPayHelperPage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "Substitution has never happened" +
              "AND the client would not reject a substitute" +
              "AND the worker would pay the substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, No)
                  .set(RejectSubstitutePage, false)
                  .set(WouldWorkerPaySubstitutePage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "Substitution has never happened" +
              "AND the client would reject a substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, No)
                  .set(RejectSubstitutePage, true)
                  .set(NeededToPayHelperPage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "Substitution happened and Client agreed" +
              "AND did not pay the substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, YesClientAgreed)
                  .set(DidPaySubstitutePage, false)
                  .set(NeededToPayHelperPage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "Substitution happened and Client agreed" +
              "AND did pay the substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, YesClientAgreed)
                  .set(DidPaySubstitutePage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "Substitution happened and Client did not agreed" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, true)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(ArrangedSubstitutePage, YesClientNotAgreed)
                  .set(NeededToPayHelperPage, false)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }
          }

          "Contract has NOT Started" when {

            "The client would not reject a substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, false)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(RejectSubstitutePage, false)
                  .set(WouldWorkerPaySubstitutePage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }

            "The client would reject a substitute" should {

              "return Valid (Right(true))" in {

                lazy val userAnswers: UserAnswers = UserAnswers("id")
                  //Setup Section
                  .set(WhoAreYouPage, WhoAreYou.Worker)
                  .set(WhatDoYouWantToFindOutPage, WhatDoYouWantToFindOut.PAYE)
                  .set(WorkerUsingIntermediaryPage, true)
                  .set(ContractStartedPage, false)
                  //Early Exit
                  .set(OfficeHolderPage, false)
                  //Personal Service Section
                  .set(RejectSubstitutePage, true)
                  //Control Section
                  .set(MoveWorkerPage, CanMoveWorkerWithPermission)
                  .set(HowWorkIsDonePage, NoWorkerInputAllowed)
                  .set(ScheduleOfWorkingHoursPage, WorkerDecideSchedule)
                  .set(ChooseWhereWorkPage, WorkerCannotChoose)
                  //Financial Risk Section
                  .set(MaterialsPage, false)
                  .set(VehiclePage, true)
                  .set(OtherExpensesPage, false)
                  .set(EquipmentExpensesPage, true)
                  .set(HowWorkerIsPaidPage, PieceRate)
                  .set(PutRightAtOwnCostPage, AsPartOfUsualRateInWorkingHours)
                  //Part and Parcel Section
                  .set(BenefitsPage, true)
                  .set(LineManagerDutiesPage, true)
                  .set(IdentifyToStakeholdersPage, WorkForEndClient)
                  //Business On Own Account
                  .set(MultipleContractsPage, true)
                  .set(PermissionToWorkWithOthersPage, true)
                  .set(OwnershipRightsPage, true)
                  .set(RightsOfWorkPage, false)
                  .set(TransferOfRightsPage, true)
                  .set(PreviousContractPage, true)
                  .set(FollowOnContractPage, false)
                  .set(FirstContractPage, false)
                  .set(ExtendContractPage, true)
                  .set(MajorityOfWorkingTimePage, true)
                  .set(SimilarWorkOtherClientsPage, true)

                lazy val result = CheckYourAnswersService.isValid(userAnswers)

                result mustBe Right(true)
              }
            }
          }
        }
      }
    }
  }
}
