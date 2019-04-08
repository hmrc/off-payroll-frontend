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
import play.api.libs.json.{JsString, Json, Writes}

case class Interview(correlationId: String,
                     endUserRole: Option[AboutYouAnswer],
                     hasContractStarted: Option[Boolean],
                     provideServices: Option[WorkerType],
                     officeHolder: Option[Boolean],
                     workerSentActualSubstitute: Option[ArrangedSubstitue],
                     workerPayActualSubstitute: Option[Boolean],
                     possibleSubstituteRejection: Option[Boolean],
                     possibleSubstituteWorkerPay: Option[Boolean],
                     wouldWorkerPayHelper: Option[Boolean],
                     engagerMovingWorker: Option[MoveWorker],
                     workerDecidingHowWorkIsDone: Option[HowWorkIsDone],
                     whenWorkHasToBeDone: Option[ScheduleOfWorkingHours],
                     workerDecideWhere: Option[ChooseWhereWork],
                     workerProvidedMaterials: Option[Boolean],
                     workerProvidedEquipment: Option[Boolean],
                     workerUsedVehicle: Option[Boolean],
                     workerHadOtherExpenses: Option[Boolean],
                     expensesAreNotRelevantForRole: Option[Boolean],
                     workerMainIncome: Option[HowWorkerIsPaid],
                     paidForSubstandardWork: Option[PutRightAtOwnCost],
                     workerReceivesBenefits: Option[Boolean],
                     workerAsLineManager: Option[Boolean],
                     contactWithEngagerCustomer: Option[Boolean],
                     workerRepresentsEngagerBusiness: Option[IdentifyToStakeholders])(implicit val appConfig: FrontendAppConfig)

object Interview {

  private implicit val writesBool: Writes[Boolean] = Writes {
    case true => JsString("Yes")
    case false => JsString("No")
  }

  implicit def writes: Writes[Interview] = Writes { model =>
    Json.obj(
      "version" -> model.appConfig.decisionVersion,
      "correlationID" -> model.correlationId,
      "interview" -> Json.obj(
        "setup" -> Json.obj(
          "endUserRole" -> model.endUserRole,
          "hasContractStarted" -> model.hasContractStarted,
          "provideServices" -> model.provideServices
        ),
        "exit" -> Json.obj(
          "officeHolder" -> model.officeHolder
        ),
        "personalService" -> Json.obj(
          "workerSentActualSubstitute" -> model.workerSentActualSubstitute,
          "workerPayActualSubstitute" -> model.workerPayActualSubstitute,
          "possibleSubstituteRejection" -> model.possibleSubstituteRejection,
          "possibleSubstituteWorkerPay" -> model.possibleSubstituteWorkerPay,
          "wouldWorkerPayHelper" -> model.wouldWorkerPayHelper
        ),
        "control" -> Json.obj(
          "engagerMovingWorker" -> model.engagerMovingWorker,
          "workerDecidingHowWorkIsDone" -> model.workerDecidingHowWorkIsDone,
          "whenWorkHasToBeDone" -> model.whenWorkHasToBeDone,
          "workerDecideWhere" -> model.workerDecideWhere
        ),
        "financialRisk" -> Json.obj(
          "workerProvidedMaterials" -> model.workerProvidedMaterials,
          "workerProvidedEquipment" -> model.workerProvidedEquipment,
          "workerUsedVehicle" -> model.workerUsedVehicle,
          "workerHadOtherExpenses" -> model.workerHadOtherExpenses,
          "expensesAreNotRelevantForRole" -> model.expensesAreNotRelevantForRole,
          "workerMainIncome" -> model.workerMainIncome,
          "paidForSubstandardWork" -> model.paidForSubstandardWork
        ),
        "partAndParcel" -> Json.obj(
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
