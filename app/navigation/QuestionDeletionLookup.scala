/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package navigation

import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import models._
import models.sections.personalService.ArrangedSubstitute._
import models.sections.setup.{WhatDoYouWantToDo, WhatDoYouWantToFindOut, WhoAreYou}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk._
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup._

@Singleton
class QuestionDeletionLookup @Inject()(implicit appConfig: FrontendAppConfig) {

  def getPagesToRemove(currentPage: QuestionPage[_]): UserAnswers => List[QuestionPage[_]] = {
    pagesToRemove.getOrElse(currentPage,_ => List.empty)
  }

  private val personalService: List[QuestionPage[_]] = List(
    ArrangedSubstitutePage, WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage, NeededToPayHelperPage
  )
  private val businessOnOwnAccount: List[QuestionPage[_]] = List(
    WorkerKnownPage, MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
    PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, SimilarWorkOtherClientsPage
  )
  private val control: List[QuestionPage[_]] = List(ChooseWhereWorkPage,MoveWorkerPage,ScheduleOfWorkingHoursPage,HowWorkIsDonePage)
  private val financialRisk: List[QuestionPage[_]] = List(
    EquipmentExpensesPage,HowWorkerIsPaidPage,MaterialsPage,OtherExpensesPage,PutRightAtOwnCostPage,VehiclePage
  )
  private val partAndParcel: List[QuestionPage[_]] = List(BenefitsPage,IdentifyToStakeholdersPage,LineManagerDutiesPage)

  private val pagesToRemove: Map[QuestionPage[_], UserAnswers => List[QuestionPage[_]]] = Map(

    //Setup Section

    WhatDoYouWantToFindOutPage -> (_ => List(WhoAreYouPage, WorkerUsingIntermediaryPage, WhatDoYouWantToDoPage)),
    WhoAreYouPage -> (answers => (answers.get(WhoAreYouPage),answers.get(WhatDoYouWantToFindOutPage)) match {
      case (Some(WhoAreYou.Agency),_) => List(WhatDoYouWantToDoPage, WorkerUsingIntermediaryPage)
      case (Some(WhoAreYou.Hirer),_) => List(WhatDoYouWantToDoPage)
      case (Some(WhoAreYou.Worker),Some(WhatDoYouWantToFindOut.PAYE)) => List(WhatDoYouWantToDoPage)
      case _ => List()
    }),
    WhatDoYouWantToDoPage -> (answers => answers.get(WhatDoYouWantToDoPage) match {
      case Some(WhatDoYouWantToDo.CheckDetermination) => List(WorkerUsingIntermediaryPage)
      case _ => List()
    }),
    ContractStartedPage -> (_ => personalService ++ businessOnOwnAccount),

    //Personal Service Section
    ArrangedSubstitutePage -> (answers => {
      answers.get(ArrangedSubstitutePage) match {
        case Some(No) => List(DidPaySubstitutePage, NeededToPayHelperPage)
        case Some(YesClientAgreed) => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, NeededToPayHelperPage)
        case Some(YesClientNotAgreed) => List(WouldWorkerPaySubstitutePage, RejectSubstitutePage, DidPaySubstitutePage)
        case _ => List.empty
      }
    }),
    DidPaySubstitutePage -> (answers => {
      answers.get(DidPaySubstitutePage) match {
        case Some(true) => List(NeededToPayHelperPage)
        case _ => List.empty
      }
    }),
    RejectSubstitutePage -> (answers => {
      answers.get(RejectSubstitutePage) match {
        case Some(true) => List(WouldWorkerPaySubstitutePage)
        case _ => List(NeededToPayHelperPage)
      }
    }),
    WouldWorkerPaySubstitutePage -> (answers => {
      answers.get(WouldWorkerPaySubstitutePage) match {
        case Some(true) => List(NeededToPayHelperPage)
        case _ => List.empty
      }
    }),
    OfficeHolderPage -> (_ => personalService ++ control ++ financialRisk ++ partAndParcel ++ businessOnOwnAccount),
    AddReferenceDetailsPage -> (answers =>
      answers.get(AddReferenceDetailsPage) match {
        case Some(false) => List(CustomisePDFPage)
        case _ => List.empty
      }),

    //BoOA Section
    WorkerKnownPage -> (
      answers => if(answers.get(WorkerKnownPage).contains(true)) List.empty else List(
        MultipleContractsPage, PermissionToWorkWithOthersPage, OwnershipRightsPage, RightsOfWorkPage, TransferOfRightsPage,
        PreviousContractPage, FollowOnContractPage, FirstContractPage, ExtendContractPage, MajorityOfWorkingTimePage, SimilarWorkOtherClientsPage
      )),
    MultipleContractsPage -> (
      answers => answers.get(MultipleContractsPage) match {
        case Some(true) => List(PermissionToWorkWithOthersPage)
        case _ => List.empty
      }),
    OwnershipRightsPage -> (
      answers => answers.get(OwnershipRightsPage) match {
        case Some(false) => List(RightsOfWorkPage, TransferOfRightsPage)
        case _ => List.empty
      }),
    RightsOfWorkPage -> (
      answers => answers.get(RightsOfWorkPage) match {
        case Some(true) => List(TransferOfRightsPage)
        case _ => List.empty
      }),
    PreviousContractPage -> (
      answers => answers.get(PreviousContractPage) match {
        case Some(false) => List(FollowOnContractPage)
        case _ => List.empty
      }),
    FollowOnContractPage -> (
      answers => answers.get(FollowOnContractPage) match {
        case Some(true) => List(ExtendContractPage)
        case _ => List.empty
      }),
    FirstContractPage -> (
      answers => answers.get(FirstContractPage) match {
        case Some(true) => List(ExtendContractPage)
        case _ => List.empty
      })
  )
}
