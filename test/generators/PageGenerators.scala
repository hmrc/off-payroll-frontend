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

package generators

import org.scalacheck.Arbitrary
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{AboutYouPage, ContractStartedPage, WhatDoYouWantToFindOutPage, WorkerTypePage}

trait PageGenerators {

  implicit lazy val whatDoYouWantToFindOutPage: Arbitrary[WhatDoYouWantToFindOutPage.type] =
    Arbitrary(WhatDoYouWantToFindOutPage)

  implicit lazy val arbitrarySimilarWorkOtherClientsPage: Arbitrary[SimilarWorkOtherClientsPage.type] =
    Arbitrary(SimilarWorkOtherClientsPage)

  implicit lazy val arbitraryOwnershipRightsPage: Arbitrary[OwnershipRightsPage.type] =
    Arbitrary(OwnershipRightsPage)

  implicit lazy val arbitraryWorkerKnownPage: Arbitrary[WorkerKnownPage.type] =
    Arbitrary(WorkerKnownPage)

  implicit lazy val arbitraryRightsOfWorkPage: Arbitrary[RightsOfWorkPage.type] =
    Arbitrary(RightsOfWorkPage)

  implicit lazy val arbitraryExtendContractPage: Arbitrary[ExtendContractPage.type] =
    Arbitrary(ExtendContractPage)

  implicit lazy val arbitraryMajorityOfWorkingTimePage: Arbitrary[MajorityOfWorkingTimePage.type] =
    Arbitrary(MajorityOfWorkingTimePage)

  implicit lazy val arbitraryFollowOnContractPage: Arbitrary[FollowOnContractPage.type] =
    Arbitrary(FollowOnContractPage)

  implicit lazy val arbitraryPreviousContractPage: Arbitrary[PreviousContractPage.type] =
    Arbitrary(PreviousContractPage)

  implicit lazy val arbitraryPermissionToWorkWithOthersPage: Arbitrary[PermissionToWorkWithOthersPage.type] =
    Arbitrary(PermissionToWorkWithOthersPage)

  implicit lazy val arbitraryMultipleContractsPage: Arbitrary[MultipleContractsPage.type] =
    Arbitrary(MultipleContractsPage)

  implicit lazy val arbitraryFirstContractPage: Arbitrary[FirstContractPage.type] =
    Arbitrary(FirstContractPage)

  implicit lazy val arbitraryTransferOfRightsPage: Arbitrary[TransferOfRightsPage.type] =
    Arbitrary(TransferOfRightsPage)

  implicit lazy val arbitraryEquipmentExpensesPage: Arbitrary[EquipmentExpensesPage.type] =
    Arbitrary(EquipmentExpensesPage)

  implicit lazy val arbitraryOtherExpensesPage: Arbitrary[OtherExpensesPage.type] =
    Arbitrary(OtherExpensesPage)

  implicit lazy val arbitraryVehiclePage: Arbitrary[VehiclePage.type] =
    Arbitrary(VehiclePage)

  implicit lazy val arbitraryMaterialsPage: Arbitrary[MaterialsPage.type] =
    Arbitrary(MaterialsPage)

  implicit lazy val arbitraryCustomisePDFPage: Arbitrary[CustomisePDFPage.type] =
    Arbitrary(CustomisePDFPage)

  implicit lazy val arbitraryDidPaySubstitutePage: Arbitrary[DidPaySubstitutePage.type] =
    Arbitrary(DidPaySubstitutePage)

  implicit lazy val arbitraryRejectSubstitutePage: Arbitrary[RejectSubstitutePage.type] =
    Arbitrary(RejectSubstitutePage)

  implicit lazy val arbitraryWouldWorkerPaySubstitutePage: Arbitrary[WouldWorkerPaySubstitutePage.type] =
    Arbitrary(WouldWorkerPaySubstitutePage)

  implicit lazy val arbitraryNeededToPayHelperPage: Arbitrary[NeededToPayHelperPage.type] =
    Arbitrary(NeededToPayHelperPage)

  implicit lazy val arbitraryMoveWorkerPage: Arbitrary[MoveWorkerPage.type] =
    Arbitrary(MoveWorkerPage)

  implicit lazy val arbitraryHowWorkIsDonePage: Arbitrary[HowWorkIsDonePage.type] =
    Arbitrary(HowWorkIsDonePage)

  implicit lazy val arbitraryScheduleOfWorkingHoursPage: Arbitrary[ScheduleOfWorkingHoursPage.type] =
    Arbitrary(ScheduleOfWorkingHoursPage)

  implicit lazy val arbitraryChooseWhereWorkPage: Arbitrary[ChooseWhereWorkPage.type] =
    Arbitrary(ChooseWhereWorkPage)

  implicit lazy val arbitraryHowWorkerIsPaidPage: Arbitrary[HowWorkerIsPaidPage.type] =
    Arbitrary(HowWorkerIsPaidPage)

  implicit lazy val arbitraryPutRightAtOwnCostPage: Arbitrary[PutRightAtOwnCostPage.type] =
    Arbitrary(PutRightAtOwnCostPage)

  implicit lazy val arbitraryBenefitsPage: Arbitrary[BenefitsPage.type] =
    Arbitrary(BenefitsPage)

  implicit lazy val arbitraryLineManagerDutiesPage: Arbitrary[LineManagerDutiesPage.type] =
    Arbitrary(LineManagerDutiesPage)

  implicit lazy val arbitraryInteractWithStakeholdersPage: Arbitrary[InteractWithStakeholdersPage.type] =
    Arbitrary(InteractWithStakeholdersPage)

  implicit lazy val arbitraryIdentifyToStakeholdersPage: Arbitrary[IdentifyToStakeholdersPage.type] =
    Arbitrary(IdentifyToStakeholdersPage)

  implicit lazy val arbitraryArrangedSubstitutePage: Arbitrary[ArrangedSubstitutePage.type] =
    Arbitrary(ArrangedSubstitutePage)

  implicit lazy val arbitraryCannotClaimAsExpensePage: Arbitrary[CannotClaimAsExpensePage.type] =
    Arbitrary(CannotClaimAsExpensePage)

  implicit lazy val arbitraryOfficeHolderPage: Arbitrary[OfficeHolderPage.type] =
    Arbitrary(OfficeHolderPage)

  implicit lazy val arbitraryWorkerTypePage: Arbitrary[WorkerTypePage.type] =
    Arbitrary(WorkerTypePage)

  implicit lazy val arbitraryContractStartedPage: Arbitrary[ContractStartedPage.type] =
    Arbitrary(ContractStartedPage)

  implicit lazy val arbitraryAboutYouPage: Arbitrary[AboutYouPage.type] =
    Arbitrary(AboutYouPage)
}
