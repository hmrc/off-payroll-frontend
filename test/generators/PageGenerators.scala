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

trait PageGenerators {

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

  implicit lazy val arbitraryArrangedSubstituePage: Arbitrary[ArrangedSubstituePage.type] =
    Arbitrary(ArrangedSubstituePage)

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
