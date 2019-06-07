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
import models.CannotClaimAsExpense._
import models.UserType._
import models.WorkerType.{LimitedCompany, SoleTrader}
import models.logging.LogInterview.isEnabled
import models.requests.DataRequest
import pages._
import pages.sections.control.{ChooseWhereWorkPage, HowWorkIsDonePage, MoveWorkerPage, ScheduleOfWorkingHoursPage}
import pages.sections.exit.OfficeHolderPage
import pages.sections.financialRisk.{CannotClaimAsExpensePage, HowWorkerIsPaidPage, PutRightAtOwnCostPage}
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
                     workerRepresentsEngagerBusiness: Option[IdentifyToStakeholders] = None)(implicit val appConfig: FrontendAppConfig){

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

object Interview extends JsonObjectSugar {

  private implicit val writesBool: Writes[Boolean] = Writes {
    case true => JsString("Yes")
    case false => JsString("No")
  }

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
        )
      )
    )
  }

  def writesPartAndParcel: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> jsonObjNoNulls(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.calculateProvideServices
        ),
        "partAndParcel" -> jsonObjNoNulls(
          "workerReceivesBenefits" -> model.workerReceivesBenefits,
          "workerAsLineManager" -> model.workerAsLineManager,
          "contactWithEngagerCustomer" -> model.contactWithEngagerCustomer,
          "workerRepresentsEngagerBusiness" -> model.workerRepresentsEngagerBusiness
        )
      )
    )
  }

  def writesFinancialRisk: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> jsonObjNoNulls(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.calculateProvideServices
        ),
        "financialRisk" -> jsonObjNoNulls(
          "workerProvidedMaterials" -> model.workerProvidedMaterials,
          "workerProvidedEquipment" -> model.workerProvidedEquipment,
          "workerUsedVehicle" -> model.workerUsedVehicle,
          "workerHadOtherExpenses" -> model.workerHadOtherExpenses,
          "expensesAreNotRelevantForRole" -> model.expensesAreNotRelevantForRole,
          "workerMainIncome" -> model.workerMainIncome,
          "paidForSubstandardWork" -> model.paidForSubstandardWork
        )
      )
    )
  }

  def writesControl: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> jsonObjNoNulls(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.calculateProvideServices
        ),
        "control" -> jsonObjNoNulls(
          "engagerMovingWorker" -> model.engagerMovingWorker,
          "workerDecidingHowWorkIsDone" -> model.workerDecidingHowWorkIsDone,
          "whenWorkHasToBeDone" -> model.whenWorkHasToBeDone,
          "workerDecideWhere" -> model.workerDecideWhere
        )
      )
    )
  }

  def writesPersonalService: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> jsonObjNoNulls(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.calculateProvideServices
        ),
        "personalService" -> jsonObjNoNulls(
          "workerSentActualSubstitute" -> model.workerSentActualSubstitute,
          "workerPayActualSubstitute" -> model.workerPayActualSubstitute,
          "possibleSubstituteRejection" -> Json.toJson(model.possibleSubstituteRejection)(writesPossibleSubstituteRejection),
          "possibleSubstituteWorkerPay" -> model.possibleSubstituteWorkerPay,
          "wouldWorkerPayHelper" -> model.wouldWorkerPayHelper
        )
      )
    )
  }

  def apply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig, request: DataRequest[_]): Interview =
    Interview(
      correlationId = userAnswers.cacheMap.id,
      endUserRole = request.userType,
      hasContractStarted = userAnswers.get(ContractStartedPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      provideServices = userAnswers.get(WorkerTypePage).fold(None: Option[WorkerType]){ answer => Some(answer.answer)},
      isUsingIntermediary = userAnswers.get(WorkerUsingIntermediaryPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      officeHolder = userAnswers.get(OfficeHolderPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      workerSentActualSubstitute = userAnswers.get(ArrangedSubstitutePage).fold(None: Option[ArrangedSubstitute]){ answer => Some(answer.answer)},
      workerPayActualSubstitute = userAnswers.get(DidPaySubstitutePage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      possibleSubstituteRejection = userAnswers.get(RejectSubstitutePage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      possibleSubstituteWorkerPay = userAnswers.get(WouldWorkerPaySubstitutePage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      wouldWorkerPayHelper = userAnswers.get(NeededToPayHelperPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      engagerMovingWorker = userAnswers.get(MoveWorkerPage).fold(None: Option[MoveWorker]){ answer => Some(answer.answer)},
      workerDecidingHowWorkIsDone = userAnswers.get(HowWorkIsDonePage).fold(None: Option[HowWorkIsDone]){ answer => Some(answer.answer)},
      whenWorkHasToBeDone = userAnswers.get(ScheduleOfWorkingHoursPage).fold(None: Option[ScheduleOfWorkingHours]){ answer => Some(answer.answer)},
      workerDecideWhere = userAnswers.get(ChooseWhereWorkPage).fold(None: Option[ChooseWhereWork]){ answer => Some(answer.answer)},
      workerProvidedMaterials = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerProvidedMaterials)),
      workerProvidedEquipment = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerProvidedEquipment)),
      workerUsedVehicle = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerUsedVehicle)),
      workerHadOtherExpenses = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(WorkerHadOtherExpenses)),
      expensesAreNotRelevantForRole = userAnswers.get(CannotClaimAsExpensePage).map(result => result.answer.contains(ExpensesAreNotRelevantForRole)),
      workerMainIncome = userAnswers.get(HowWorkerIsPaidPage).fold(None: Option[HowWorkerIsPaid]){ answer => Some(answer.answer)},
      paidForSubstandardWork = userAnswers.get(PutRightAtOwnCostPage).fold(None: Option[PutRightAtOwnCost]){ answer => Some(answer.answer)},
      workerReceivesBenefits = userAnswers.get(BenefitsPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      workerAsLineManager = userAnswers.get(LineManagerDutiesPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      contactWithEngagerCustomer = userAnswers.get(InteractWithStakeholdersPage).fold(None: Option[Boolean]){ answer => Some(answer.answer)},
      workerRepresentsEngagerBusiness = userAnswers.get(IdentifyToStakeholdersPage).fold(None: Option[IdentifyToStakeholders]){ answer => Some(answer.answer)}
    )
}
