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
import pages._
import play.api.libs.json.{JsNull, JsString, Json, Writes}
import utils.JsonObjectSugar

case class Interview(correlationId: String,
                     endUserRole: Option[AboutYouAnswer] = None,
                     hasContractStarted: Option[Boolean] = None,
                     provideServices: Option[WorkerType] = None,
                     officeHolder: Option[Boolean] = None,
                     workerSentActualSubstitute: Option[ArrangedSubstitue] = None,
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
                     workerRepresentsEngagerBusiness: Option[IdentifyToStakeholders] = None)(implicit val appConfig: FrontendAppConfig)

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
          "provideServices" -> model.provideServices
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

  def apply(userAnswers: UserAnswers)(implicit appConfig: FrontendAppConfig): Interview =
    Interview(
      correlationId = userAnswers.cacheMap.id,
      endUserRole = userAnswers.get(AboutYouPage),
      hasContractStarted = userAnswers.get(ContractStartedPage),
      provideServices = userAnswers.get(WorkerTypePage),
      officeHolder = userAnswers.get(OfficeHolderPage),
      workerSentActualSubstitute = userAnswers.get(ArrangedSubstituePage),
      workerPayActualSubstitute = userAnswers.get(DidPaySubstitutePage),
      possibleSubstituteRejection = userAnswers.get(RejectSubstitutePage),
      possibleSubstituteWorkerPay = userAnswers.get(WouldWorkerPaySubstitutePage),
      wouldWorkerPayHelper = userAnswers.get(NeededToPayHelperPage),
      engagerMovingWorker = userAnswers.get(MoveWorkerPage),
      workerDecidingHowWorkIsDone = userAnswers.get(HowWorkIsDonePage),
      whenWorkHasToBeDone = userAnswers.get(ScheduleOfWorkingHoursPage),
      workerDecideWhere = userAnswers.get(ChooseWhereWorkPage),
      workerProvidedMaterials = userAnswers.get(CannotClaimAsExpensePage).map(_.exists(_ == WorkerProvidedMaterials)),
      workerProvidedEquipment = userAnswers.get(CannotClaimAsExpensePage).map(_.exists(_ == WorkerProvidedEquipment)),
      workerUsedVehicle = userAnswers.get(CannotClaimAsExpensePage).map(_.exists(_ == WorkerUsedVehicle)),
      workerHadOtherExpenses = userAnswers.get(CannotClaimAsExpensePage).map(_.exists(_ == WorkerHadOtherExpenses)),
      expensesAreNotRelevantForRole = userAnswers.get(CannotClaimAsExpensePage).map(_.exists(_ == ExpensesAreNotRelevantForRole)),
      workerMainIncome = userAnswers.get(HowWorkerIsPaidPage),
      paidForSubstandardWork = userAnswers.get(PutRightAtOwnCostPage),
      workerReceivesBenefits = userAnswers.get(BenefitsPage),
      workerAsLineManager = userAnswers.get(LineManagerDutiesPage),
      contactWithEngagerCustomer = userAnswers.get(InteractWithStakeholdersPage),
      workerRepresentsEngagerBusiness = userAnswers.get(IdentifyToStakeholdersPage)
    )
}
