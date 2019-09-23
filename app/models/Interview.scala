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

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import models.CannotClaimAsExpense._
import models.IdentifyToStakeholders.WouldNotHappen
import models.Interview.jsonObjNoNulls
import models.WorkerType.{LimitedCompany, SoleTrader}
import models.requests.DataRequest
import pages.{FollowOnContractPage, MajorityOfWorkingTimePage, MultipleContractsPage, PermissionToWorkWithOthersPage, PreviousContractPage, QuestionPage, RightsOfWorkPage, SimilarWorkOtherClientsPage, TransferOfRightsPage}
import pages.sections.businessOnOwnAccount.{ExclusiveContractPage, ExtendContractPage, FinanciallyDependentPage, FirstContractPage, MultipleEngagementsPage, OwnershipRightsPage, SeriesOfContractsPage, SignificantWorkingTimePage, TransferRightsPage}
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{VehiclePage, _}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, InteractWithStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{ContractStartedPage, WorkerTypePage, WorkerUsingIntermediaryPage}
import play.api.libs.json._
import utils.JsonObjectSugar

case class Interview(correlationId: String,
                     endUserRole: Option[UserType] = None,
                     hasContractStarted: Option[Boolean] = None,
                     provideServices: Option[WorkerType] = None,
                     isUsingIntermediary : Option[Boolean] = None,
                     officeHolder: Option[Boolean] = None,
                     workerSentActualSubstitute: Option[ArrangedSubstitute] = None,
                     workerPayActualSubstitute: Option[Boolean] = None,
                     possibleSubstituteRejection: Option[Boolean] = None,
                     possibleSubstituteWorkerPay: Option[Boolean] = None,
                     wouldWorkerPayHelper: Option[Boolean] = None,
                     engagerMovingWorker: Option[MoveWorker] = None,
                     workerDecidingHowWorkIsDone: Option[HowWorkIsDone] = None,
                     whenWorkHasToBeDone: Option[ScheduleOfWorkingHours] = None,
                     workerDecideWhere: Option[ChooseWhereWork] = None,
                     workerProvidedMaterials: Option[Boolean] = None,
                     workerProvidedEquipment: Option[Boolean] = None,
                     workerUsedVehicle: Option[Boolean] = None,
                     workerHadOtherExpenses: Option[Boolean] = None,
                     expensesAreNotRelevantForRole: Option[Boolean] = None,
                     workerMainIncome: Option[HowWorkerIsPaid] = None,
                     paidForSubstandardWork: Option[PutRightAtOwnCost] = None,
                     workerReceivesBenefits: Option[Boolean] = None,
                     workerAsLineManager: Option[Boolean] = None,
                     contactWithEngagerCustomer: Option[Boolean] = None,
                     workerRepresentsEngagerBusiness: Option[IdentifyToStakeholders] = None,
                     //BOOA
                     exclusiveContract: Option[ExclusiveContract] = None,
                     transferRights: Option[TransferRights] = None,
                     multipleEngagements: Option[MultipleEngagements] = None,
                     significantWorkingTime: Option[SignificantWorkingTime] = None,
                     seriesOfContracts: Option[SeriesOfContracts] = None
                    )(implicit val appConfig: FrontendAppConfig){

  def calculateProvideServices: Option[WorkerType] = {

    (isUsingIntermediary, provideServices) match {

      case (Some(usingIntermediary), _) => if(usingIntermediary){
        Some(LimitedCompany)
      } else {
        Some(SoleTrader)
      }
      case (None, Some(providedServices)) => Some(providedServices)
      case _ => None
    }
  }

  def route: String =
    (isUsingIntermediary, provideServices) match {
      case (Some(usingIntermediary), _) => if (usingIntermediary) "IR35" else "ESI"
      case (_, Some(providedServices)) => if (providedServices == SoleTrader) "ESI" else "IR35"
      case _ => "IR35"
    }
}



object Interview extends JsonObjectSugar with FeatureSwitching {

  private val writesPossibleSubstituteRejection: Writes[Option[Boolean]] = Writes {
    case Some(true) => JsString("wouldReject")
    case Some(_) => JsString("wouldNotReject")
    case _ => JsNull
  }

  implicit def writes: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> jsonObjNoNulls(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.calculateProvideServices
        ),
        "exit" -> jsonObjNoNulls(
          "officeHolder" -> model.officeHolder
        ),
        "personalService" -> jsonObjNoNulls(
          "workerSentActualSubstitute" -> model.workerSentActualSubstitute,
          "workerPayActualSubstitute" -> model.workerPayActualSubstitute,
          "possibleSubstituteRejection" -> Json.toJson(model.possibleSubstituteRejection)(writesPossibleSubstituteRejection),
          "possibleSubstituteWorkerPay" -> model.possibleSubstituteWorkerPay,
          "wouldWorkerPayHelper" -> model.wouldWorkerPayHelper
        ),
        "control" -> jsonObjNoNulls(
          "engagerMovingWorker" -> model.engagerMovingWorker,
          "workerDecidingHowWorkIsDone" -> model.workerDecidingHowWorkIsDone,
          "whenWorkHasToBeDone" -> model.whenWorkHasToBeDone,
          "workerDecideWhere" -> model.workerDecideWhere
        ),
        "financialRisk" -> jsonObjNoNulls(
          "workerProvidedMaterials" -> model.workerProvidedMaterials,
          "workerProvidedEquipment" -> model.workerProvidedEquipment,
          "workerUsedVehicle" -> model.workerUsedVehicle,
          "workerHadOtherExpenses" -> model.workerHadOtherExpenses,
          "expensesAreNotRelevantForRole" -> model.expensesAreNotRelevantForRole,
          "workerMainIncome" -> model.workerMainIncome,
          "paidForSubstandardWork" -> model.paidForSubstandardWork
        ),
        "partAndParcel" -> jsonObjNoNulls(
          "workerReceivesBenefits" -> model.workerReceivesBenefits,
          "workerAsLineManager" -> model.workerAsLineManager,
          "contactWithEngagerCustomer" -> model.contactWithEngagerCustomer,
          "workerRepresentsEngagerBusiness" -> model.workerRepresentsEngagerBusiness
        ),
        "businessOnOwnAccount" -> jsonObjNoNulls(
          "exclusiveContract" -> model.exclusiveContract,
          "transferRights" -> model.transferRights,
          "multipleEngagements" -> model.multipleEngagements,
          "significantWorkingTime" -> model.significantWorkingTime,
          "seriesOfContracts" -> model.seriesOfContracts
        )
      )
    )
  }

  def apply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig, request: DataRequest[_]): Interview = {

    if(isEnabled(OptimisedFlow)) optimisedApply(userAnswers) else subOptimisedApply(userAnswers)
  }

  private def getAnswer[A](page: QuestionPage[A])(implicit userAnswers: UserAnswers, rds: Reads[A]): Option[A] ={
    userAnswers.get(page).fold(None: Option[A]) { answer => Some(answer.answer) }
  }

  private def optimisedApply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig, request: DataRequest[_]): Interview = {
    implicit val implicitUserAnswers: UserAnswers = userAnswers

    val workerProvidedMaterials = getAnswer[Boolean](MaterialsPage)
    val workerProvidedEquipment = getAnswer[Boolean](EquipmentExpensesPage)
    val workerUsedVehicle = getAnswer[Boolean](VehiclePage)
    val workerHadOtherExpenses = getAnswer[Boolean](OtherExpensesPage)

    val expensesAreNotRelevantForRole = (workerProvidedMaterials, workerProvidedEquipment, workerUsedVehicle, workerHadOtherExpenses) match {
      case (Some(false), Some(false), Some(false), Some(false)) => Some(true)
      case (None, None, None, None) => None
      case _ => Some(false)
    }

    val contactWithEngagerCustomer: Option[Boolean] = userAnswers.get(IdentifyToStakeholdersPage).map(x => !(x.answer == WouldNotHappen))
    val workerRepresentsEngagerBusiness = userAnswers.get(IdentifyToStakeholdersPage).fold[Option[IdentifyToStakeholders]](None)(
      x => if(x.answer == WouldNotHappen) None else Some(x.answer))

    Interview(correlationId = userAnswers.cacheMap.id, endUserRole = request.userType,
      hasContractStarted = getAnswer[Boolean](ContractStartedPage),
      provideServices = getAnswer[WorkerType](WorkerTypePage),
      isUsingIntermediary = getAnswer[Boolean](WorkerUsingIntermediaryPage),
      officeHolder = getAnswer[Boolean](OfficeHolderPage),
      workerSentActualSubstitute = getAnswer[ArrangedSubstitute](ArrangedSubstitutePage),
      workerPayActualSubstitute = getAnswer[Boolean](DidPaySubstitutePage),
      possibleSubstituteRejection = getAnswer[Boolean](RejectSubstitutePage),
      possibleSubstituteWorkerPay = getAnswer[Boolean](WouldWorkerPaySubstitutePage),
      wouldWorkerPayHelper = getAnswer[Boolean](NeededToPayHelperPage),
      engagerMovingWorker = getAnswer[MoveWorker](MoveWorkerPage),
      workerDecidingHowWorkIsDone = getAnswer[HowWorkIsDone](HowWorkIsDonePage),
      whenWorkHasToBeDone = getAnswer[ScheduleOfWorkingHours](ScheduleOfWorkingHoursPage),
      workerDecideWhere = getAnswer[ChooseWhereWork](ChooseWhereWorkPage),
      workerProvidedMaterials = workerProvidedMaterials,
      workerProvidedEquipment = workerProvidedEquipment,
      workerUsedVehicle = workerUsedVehicle,
      workerHadOtherExpenses = workerHadOtherExpenses,
      expensesAreNotRelevantForRole = expensesAreNotRelevantForRole,
      workerMainIncome = getAnswer[HowWorkerIsPaid](HowWorkerIsPaidPage),
      paidForSubstandardWork = getAnswer[PutRightAtOwnCost](PutRightAtOwnCostPage),
      workerReceivesBenefits = getAnswer[Boolean](BenefitsPage),
      workerAsLineManager = getAnswer[Boolean](LineManagerDutiesPage),
      contactWithEngagerCustomer = contactWithEngagerCustomer,
      workerRepresentsEngagerBusiness = workerRepresentsEngagerBusiness,
      exclusiveContract = exclusiveContract(),
      transferRights = transferRights(),
      multipleEngagements = multipleEngagements(),
      significantWorkingTime = significantWorkingTime(),
      seriesOfContracts = seriesOfContracts()
    )
  }


  private def exclusiveContract()(implicit userAnswers: UserAnswers) = {
    val multipleContract = userAnswers.getAnswer(MultipleContractsPage)
    val permissionToWorkWithOthers = userAnswers.getAnswer(PermissionToWorkWithOthersPage)

    val exclusiveContract: Option[ExclusiveContract] = (multipleContract, permissionToWorkWithOthers) match {
      case (Some(true), _) => Some(ExclusiveContract.UnableToProvideServices)
      case (_, Some(true)) => Some(ExclusiveContract.AbleToProvideServicesWithPermission)
      case (_, Some(false)) => Some(ExclusiveContract.AbleToProvideServices)
      case _ => None
    }
    exclusiveContract
  }

  private def transferRights()(implicit userAnswers: UserAnswers) = {
    val ownershipRights = userAnswers.getAnswer(OwnershipRightsPage)
    val rightsOfWork = userAnswers.getAnswer(RightsOfWorkPage)
    val transferOfRights = userAnswers.getAnswer(TransferOfRightsPage)

    (ownershipRights, rightsOfWork, transferOfRights) match {
      case (Some(false), _, _) => Some(TransferRights.NoRightsArise)
      case (Some(true), Some(true), _) => Some(TransferRights.RightsTransferredToClient)
      case (Some(true), Some(false), Some(false)) => Some(TransferRights.RetainOwnershipRights)
      case (Some(true), Some(false), Some(true)) => Some(TransferRights.AbleToTransferRights)
      case _ => None
    }
  }

  private def multipleEngagements()(implicit userAnswers: UserAnswers) =
    userAnswers.getAnswer(SimilarWorkOtherClientsPage) match {
      case Some(true) => Some(MultipleEngagements.OnlyContractForPeriod)
      case Some(false) => Some(MultipleEngagements.ProvidedServicesToOtherEngagers)
      case _ => None
    }

  private def significantWorkingTime()(implicit userAnswers: UserAnswers) =
    (userAnswers.getAnswer(MajorityOfWorkingTimePage), userAnswers.getAnswer(FinanciallyDependentPage)) match {
      case (Some(true), Some(true)) => Some(SignificantWorkingTime.ConsumesSignificantAmount)
      case (Some(false), Some(false)) => Some(SignificantWorkingTime.NoSignificantAmount)
      case (Some(true), Some(false)) => Some(SignificantWorkingTime.TimeButNotMoney)
      case (Some(false), Some(true)) => Some(SignificantWorkingTime.MoneyButNotTime)
      case _ => None
    }

  private def seriesOfContracts()(implicit userAnswers: UserAnswers) = {
    val previousContractPage = userAnswers.getAnswer(PreviousContractPage)
    val followOnContract = userAnswers.getAnswer(FollowOnContractPage)
    val firstContract = userAnswers.getAnswer(FirstContractPage)
    val extendContract = userAnswers.getAnswer(ExtendContractPage)

    (followOnContract, firstContract, extendContract) match {
      case (Some(true), _, _) => Some(SeriesOfContracts.ContractIsPartOfASeries)
      case (Some(false), Some(true), _) => Some(SeriesOfContracts.ContractIsPartOfASeries)
      case (Some(false), Some(false), Some(false)) => Some(SeriesOfContracts.StandAloneContract)
      case (Some(false), Some(false), Some(true)) => Some(SeriesOfContracts.ContractCouldBeExtended)
      case (None, Some(true), _) => Some(SeriesOfContracts.ContractIsPartOfASeries)
      case (None, Some(false), Some(false)) => Some(SeriesOfContracts.StandAloneContract)
      case (None, Some(false), Some(true)) => Some(SeriesOfContracts.ContractCouldBeExtended)
      case _ => None
    }
  }

  private def subOptimisedApply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig, request: DataRequest[_]): Interview = {
    implicit val implicitUserAnswers: UserAnswers = userAnswers

    Interview(
      correlationId = userAnswers.cacheMap.id, endUserRole = request.userType,
      hasContractStarted = getAnswer[Boolean](ContractStartedPage),
      provideServices = getAnswer[WorkerType](WorkerTypePage),
      isUsingIntermediary = getAnswer[Boolean](WorkerUsingIntermediaryPage),
      officeHolder = getAnswer[Boolean](OfficeHolderPage),
      workerSentActualSubstitute = getAnswer[ArrangedSubstitute](ArrangedSubstitutePage),
      workerPayActualSubstitute = getAnswer[Boolean](DidPaySubstitutePage),
      possibleSubstituteRejection = getAnswer[Boolean](RejectSubstitutePage),
      possibleSubstituteWorkerPay = getAnswer[Boolean](WouldWorkerPaySubstitutePage),
      wouldWorkerPayHelper = getAnswer[Boolean](NeededToPayHelperPage),
      engagerMovingWorker = getAnswer[MoveWorker](MoveWorkerPage),
      workerDecidingHowWorkIsDone = getAnswer[HowWorkIsDone](HowWorkIsDonePage),
      whenWorkHasToBeDone = getAnswer[ScheduleOfWorkingHours](ScheduleOfWorkingHoursPage),
      workerDecideWhere = getAnswer[ChooseWhereWork](ChooseWhereWorkPage),
      workerProvidedMaterials = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerProvidedMaterials)),
      workerProvidedEquipment = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerProvidedEquipment)),
      workerUsedVehicle = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerUsedVehicle)),
      workerHadOtherExpenses = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerHadOtherExpenses)),
      expensesAreNotRelevantForRole = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(ExpensesAreNotRelevantForRole)),
      workerMainIncome = getAnswer[HowWorkerIsPaid](HowWorkerIsPaidPage),
      paidForSubstandardWork = getAnswer[PutRightAtOwnCost](PutRightAtOwnCostPage),
      workerReceivesBenefits = getAnswer[Boolean](BenefitsPage),
      workerAsLineManager = getAnswer[Boolean](LineManagerDutiesPage),
      contactWithEngagerCustomer = userAnswers.get(InteractWithStakeholdersPage).fold[Option[Boolean]](None)(x => Some(x.answer)),
      workerRepresentsEngagerBusiness = userAnswers.get(IdentifyToStakeholdersPage).fold[Option[IdentifyToStakeholders]](None)(x => Some(x.answer))
    )
  }
}
