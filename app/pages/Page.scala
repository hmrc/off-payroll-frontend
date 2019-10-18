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

package pages

import pages.sections.businessOnOwnAccount._
import pages.sections.control._
import pages.sections.exit._
import pages.sections.financialRisk._
import pages.sections.partParcel._
import pages.sections.personalService._
import pages.sections.setup._

import scala.collection.immutable.Map
import scala.language.implicitConversions

trait Page

object Page {

  implicit def toString(page: Page): String =
    page.toString

  def apply(pageName: String): QuestionPage[_] = questionToPage(pageName)
  def unapply(arg: QuestionPage[_]): String = questionToPage.map(_.swap).apply(arg)

  lazy val questionToPage = Map[String, QuestionPage[_]](
    AboutYouPage.toString -> AboutYouPage,
    WhichDescribesYouPage.toString -> WhichDescribesYouPage,
    WhatDoYouWantToFindOutPage.toString -> WhatDoYouWantToFindOutPage,
    WhoAreYouPage.toString -> WhoAreYouPage,
    WhatDoYouWantToDoPage.toString -> WhatDoYouWantToDoPage,
    WorkerTypePage.toString -> WorkerTypePage,
    ContractStartedPage.toString -> ContractStartedPage,
    WorkerUsingIntermediaryPage.toString -> WorkerUsingIntermediaryPage,
    OfficeHolderPage.toString -> OfficeHolderPage,
    ArrangedSubstitutePage.toString -> ArrangedSubstitutePage,
    DidPaySubstitutePage.toString -> DidPaySubstitutePage,
    WouldWorkerPaySubstitutePage.toString -> WouldWorkerPaySubstitutePage,
    NeededToPayHelperPage.toString -> NeededToPayHelperPage,
    MoveWorkerPage.toString -> MoveWorkerPage,
    RejectSubstitutePage.toString -> RejectSubstitutePage,
    HowWorkIsDonePage.toString -> HowWorkIsDonePage,
    ScheduleOfWorkingHoursPage.toString -> ScheduleOfWorkingHoursPage,
    ChooseWhereWorkPage.toString -> ChooseWhereWorkPage,
    CannotClaimAsExpensePage.toString -> CannotClaimAsExpensePage,
    EquipmentExpensesPage.toString -> EquipmentExpensesPage,
    MaterialsPage.toString -> MaterialsPage,
    OtherExpensesPage.toString -> OtherExpensesPage,
    VehiclePage.toString -> VehiclePage,
    HowWorkerIsPaidPage.toString -> HowWorkerIsPaidPage,
    PutRightAtOwnCostPage.toString -> PutRightAtOwnCostPage,
    BenefitsPage.toString -> BenefitsPage,
    LineManagerDutiesPage.toString -> LineManagerDutiesPage,
    InteractWithStakeholdersPage.toString -> InteractWithStakeholdersPage,
    IdentifyToStakeholdersPage.toString -> IdentifyToStakeholdersPage,
    CustomisePDFPage.toString -> CustomisePDFPage,
    ResultPage.toString -> ResultPage,
    Timestamp.toString -> Timestamp,
    AddReferenceDetailsPage.toString -> AddReferenceDetailsPage,
    ExtendContractPage.toString -> ExtendContractPage,
    FirstContractPage.toString -> FirstContractPage,
    FollowOnContractPage.toString -> FollowOnContractPage,
    MajorityOfWorkingTimePage.toString -> MajorityOfWorkingTimePage,
    MultipleContractsPage.toString -> MultipleContractsPage,
    OwnershipRightsPage.toString -> OwnershipRightsPage,
    PermissionToWorkWithOthersPage.toString -> PermissionToWorkWithOthersPage,
    PreviousContractPage.toString -> PreviousContractPage,
    RightsOfWorkPage.toString -> RightsOfWorkPage,
    SimilarWorkOtherClientsPage.toString -> SimilarWorkOtherClientsPage,
    TransferOfRightsPage.toString -> TransferOfRightsPage,
    WorkerKnownPage.toString -> WorkerKnownPage
  )
}
