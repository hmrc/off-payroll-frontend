/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import models.requests.DataRequest
import models.sections.businessOnOwnAccount._
import models.sections.control.{ChooseWhereWork, HowWorkIsDone, MoveWorker, ScheduleOfWorkingHours}
import models.sections.financialRisk.{HowWorkerIsPaid, PutRightAtOwnCost}
import models.sections.partAndParcel.IdentifyToStakeholders
import models.sections.partAndParcel.IdentifyToStakeholders.WouldNotHappen
import models.sections.personalService.ArrangedSubstitute
import models.sections.setup.WorkerType.{LimitedCompany, SoleTrader}
import models.sections.setup.{WhoAreYou, WorkerType}
import pages._
import pages.sections.businessOnOwnAccount._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{VehiclePage, _}
import pages.sections.partParcel.{BenefitsPage, IdentifyToStakeholdersPage, LineManagerDutiesPage}
import pages.sections.personalService._
import pages.sections.setup.{ContractStartedPage, WorkerUsingIntermediaryPage}
import play.api.libs.json._
import utils.JsonObjectSugar

case class Interview(correlationId: String,
                     endUserRole: Option[WhoAreYou] = None,
                     hasContractStarted: Option[Boolean] = None,
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

  def calculateProvideServices: Option[WorkerType] =
    isUsingIntermediary.map(ans => if(ans) LimitedCompany else SoleTrader)

  def route: String =
    isUsingIntermediary.fold("IR35")(ans => if(ans) "IR35" else "ESI")
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

  private def getAnswer[A](page: QuestionPage[A])(implicit userAnswers: UserAnswers, rds: Reads[A]): Option[A] ={
    userAnswers.get(page).fold(None: Option[A]) { answer => Some(answer) }
  }

  def apply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig, request: DataRequest[_]): Interview = {
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

    val contactWithEngagerCustomer: Option[Boolean] = userAnswers.get(IdentifyToStakeholdersPage).map(x => !(x == WouldNotHappen))
    val workerRepresentsEngagerBusiness = userAnswers.get(IdentifyToStakeholdersPage).fold[Option[IdentifyToStakeholders]](None)(
      x => if(x == WouldNotHappen) None else Some(x))

    Interview(correlationId = userAnswers.cacheMap.id, endUserRole = request.userType,
      hasContractStarted = getAnswer[Boolean](ContractStartedPage),
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
      significantWorkingTime = significantWorkingTime,
      seriesOfContracts = seriesOfContracts()
    )
  }


  private def exclusiveContract()(implicit userAnswers: UserAnswers) = {
    val multipleContract = userAnswers.get(MultipleContractsPage)
    val permissionToWorkWithOthers = userAnswers.get(PermissionToWorkWithOthersPage)

    val exclusiveContract: Option[ExclusiveContract] = (multipleContract, permissionToWorkWithOthers) match {
      case (Some(true), _) => Some(ExclusiveContract.UnableToProvideServices)
      case (_, Some(true)) => Some(ExclusiveContract.AbleToProvideServicesWithPermission)
      case (_, Some(false)) => Some(ExclusiveContract.AbleToProvideServices)
      case _ => None
    }
    exclusiveContract
  }

  private def transferRights()(implicit userAnswers: UserAnswers) = {
    val ownershipRights = userAnswers.get(OwnershipRightsPage)
    val rightsOfWork = userAnswers.get(RightsOfWorkPage)
    val transferOfRights = userAnswers.get(TransferOfRightsPage)

    (ownershipRights, rightsOfWork, transferOfRights) match {
      case (Some(false), _, _) => Some(TransferRights.NoRightsArise)
      case (Some(true), Some(true), _) => Some(TransferRights.RightsTransferredToClient)
      case (Some(true), Some(false), Some(false)) => Some(TransferRights.RetainOwnershipRights)
      case (Some(true), Some(false), Some(true)) => Some(TransferRights.AbleToTransferRights)
      case _ => None
    }
  }

  private def multipleEngagements()(implicit userAnswers: UserAnswers) =
    userAnswers.get(SimilarWorkOtherClientsPage) match {
      case Some(true) => Some(MultipleEngagements.ProvidedServicesToOtherEngagers)
      case Some(false) => Some(MultipleEngagements.OnlyContractForPeriod)
      case _ => None
    }

  private def significantWorkingTime()(implicit userAnswers: UserAnswers) =
    userAnswers.get(MajorityOfWorkingTimePage) match {
      case Some(true) => Some(SignificantWorkingTime.ConsumesSignificantAmount)
      case Some(false) => Some(SignificantWorkingTime.NoSignificantAmount)
      case _ => None
    }

  private def seriesOfContracts()(implicit userAnswers: UserAnswers) = {
    val followOnContract = userAnswers.get(FollowOnContractPage)
    val firstContract = userAnswers.get(FirstContractPage)
    val extendContract = userAnswers.get(ExtendContractPage)

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
}
