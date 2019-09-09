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

package navigation

import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import models.ArrangedSubstitute.{No, YesClientAgreed, YesClientNotAgreed}
import models._
import pages.sections.businessOnOwnAccount.{ExtendContractPage, FinanciallyDependentPage, FirstContractPage, OwnershipRightsPage, WorkerKnownPage}
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._
import pages.{RightsOfWorkPage, _}

@Singleton
class QuestionDeletionLookup @Inject()(implicit appConfig: FrontendAppConfig) extends NavigationHelper {

  def getPagesToRemove(currentPage: QuestionPage[_]): UserAnswers => List[QuestionPage[_]] = {
    pagesToRemove.getOrElse(currentPage,_ => List.empty)
  }

  private val personalService: List[QuestionPage[_]] = List(ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage)
  private val businessOnOwnAccount: List[QuestionPage[_]] = List(
    WorkerKnownPage, MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
    PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, FinanciallyDependentPage,
    SimilarWorkOtherClientsPage
  )
  private val control: List[QuestionPage[_]] = List(ChooseWhereWorkPage,MoveWorkerPage,ScheduleOfWorkingHoursPage,HowWorkIsDonePage)
  private val financialRisk: List[QuestionPage[_]] = List(EquipmentExpensesPage,HowWorkerIsPaidPage,MaterialsPage,OtherExpensesPage,PutRightAtOwnCostPage,VehiclePage,CannotClaimAsExpensePage)
  private val partAndParcel: List[QuestionPage[_]] = List(BenefitsPage,IdentifyToStakeholdersPage,LineManagerDutiesPage,InteractWithStakeholdersPage)

  private val pagesToRemove: Map[QuestionPage[_], UserAnswers => List[QuestionPage[_]]] = Map(

    //Setup Section
    WorkerUsingIntermediaryPage -> (answers => answers.getAnswer(WorkerUsingIntermediaryPage) match {
      case Some(false) =>
        List(IsWorkForPrivateSectorPage, TurnoverOverPage, EmployeesOverPage, BalanceSheetOverPage)
      case _ => List.empty
    }),
    IsWorkForPrivateSectorPage -> (answers => {
      answers.getAnswer(IsWorkForPrivateSectorPage) match {
        case Some(false) => List(TurnoverOverPage, EmployeesOverPage, BalanceSheetOverPage)
        case _ => List.empty
      }
    }),
    TurnoverOverPage -> (_ => List(EmployeesOverPage, BalanceSheetOverPage)),
    EmployeesOverPage -> (_ => List(BalanceSheetOverPage)),
    ContractStartedPage -> (_ => personalService ++ businessOnOwnAccount),

    //Personal Service Section
    ArrangedSubstitutePage -> (answers => {
      answers.getAnswer(ArrangedSubstitutePage) match {
        case Some(No) => List(DidPaySubstitutePage, NeededToPayHelperPage)
        case Some(YesClientAgreed) => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage)
        case Some(YesClientNotAgreed) => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage)
        case _ => List.empty
      }
    }),
    DidPaySubstitutePage -> (answers => {
      answers.getAnswer(DidPaySubstitutePage) match {
        case Some(true) => List(NeededToPayHelperPage)
        case _ => List.empty
      }
    }),
    RejectSubstitutePage -> (answers => {
      answers.getAnswer(RejectSubstitutePage) match {
        case Some(true) => List(WouldWorkerPaySubstitutePage)
        case _ => List(NeededToPayHelperPage)
      }
    }),
    WouldWorkerPaySubstitutePage -> (answers => {
      answers.getAnswer(WouldWorkerPaySubstitutePage) match {
        case Some(true) => List(NeededToPayHelperPage)
        case _ => List.empty
      }
    }),
    OfficeHolderPage -> (_ => personalService ++ control ++ financialRisk ++ partAndParcel ++ businessOnOwnAccount),
    AddReferenceDetailsPage -> (answers =>
      answers.getAnswer(AddReferenceDetailsPage) match {
        case Some(false) => List(CustomisePDFPage)
        case _ => List.empty
      }),

    //BoOA Section
    WorkerKnownPage -> (
      answers => if(answers.getAnswer(WorkerKnownPage).contains(true)) List.empty else List(
        MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
        PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, FinanciallyDependentPage,
        SimilarWorkOtherClientsPage
      )),
    MultipleContractsPage -> (
      answers => answers.getAnswer(MultipleContractsPage) match {
        case Some(true) => List(PermissionToWorkWithOthersPage)
        case _ => List.empty
      }),
    OwnershipRightsPage -> (
      answers => answers.getAnswer(OwnershipRightsPage) match {
        case Some(false) => List(RightsOfWorkPage, TransferOfRightsPage)
        case _ => List.empty
      }),
    RightsOfWorkPage -> (
      answers => answers.getAnswer(RightsOfWorkPage) match {
        case Some(true) => List(TransferOfRightsPage)
        case _ => List.empty
      }),
    PreviousContractPage -> (
      answers => answers.getAnswer(PreviousContractPage) match {
        case Some(false) => List(FollowOnContractPage)
        case _ => List.empty
      }),
    FollowOnContractPage -> (
      answers => answers.getAnswer(FollowOnContractPage) match {
        case Some(true) => List(ExtendContractPage)
        case _ => List.empty
      }),
    FirstContractPage -> (
      answers => answers.getAnswer(FirstContractPage) match {
        case Some(true) => List(ExtendContractPage)
        case _ => List.empty
      })
  )
}
