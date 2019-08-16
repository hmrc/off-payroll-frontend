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
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._

@Singleton
class QuestionDeletionLookup @Inject()(implicit appConfig: FrontendAppConfig) {

  def getPagesToRemove(currentPage: QuestionPage[_]): UserAnswers => List[QuestionPage[_]] = {
    pagesToRemove.getOrElse(currentPage,_ => List.empty)
  }

  private val personalService = List(ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage)

  //TODO: Needs Updating with logic for BoOA - future Story
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
    ContractStartedPage -> (_ => personalService),
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
    OfficeHolderPage -> (
      _ => List(
        //Personal Service
        ArrangedSubstitutePage,DidPaySubstitutePage,NeededToPayHelperPage,RejectSubstitutePage,WouldWorkerPaySubstitutePage,
        //Control
        ChooseWhereWorkPage,MoveWorkerPage,ScheduleOfWorkingHoursPage,HowWorkIsDonePage,
        //Financial Risk
        EquipmentExpensesPage,HowWorkerIsPaidPage,MaterialsPage,OtherExpensesPage,PutRightAtOwnCostPage,VehiclePage,CannotClaimAsExpensePage,
        //Part Parcel
        BenefitsPage,IdentifyToStakeholdersPage,LineManagerDutiesPage,InteractWithStakeholdersPage
        )
    ),
    AddReferenceDetailsPage -> (answers =>
      answers.getAnswer(AddReferenceDetailsPage) match {
        case Some(false) => List(CustomisePDFPage)
        case _ => List.empty
      }
    )
  )
}
