/*
 * Copyright 2022 HM Revenue & Customs
 *
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
    EquipmentExpensesPage.toString -> EquipmentExpensesPage,
    MaterialsPage.toString -> MaterialsPage,
    OtherExpensesPage.toString -> OtherExpensesPage,
    VehiclePage.toString -> VehiclePage,
    HowWorkerIsPaidPage.toString -> HowWorkerIsPaidPage,
    PutRightAtOwnCostPage.toString -> PutRightAtOwnCostPage,
    BenefitsPage.toString -> BenefitsPage,
    LineManagerDutiesPage.toString -> LineManagerDutiesPage,
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
