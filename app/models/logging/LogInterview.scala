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

package models.logging

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import models.WorkerType.SoleTrader
import models.{DecisionResponse, Interview}
import play.api.libs.json._
import utils.{DateTimeJsonFormat, DateTimeUtil}

case class LogInterview(version: String,
                        compressedInterview: String,
                        route: String,
                        decision:String,
                        count: Option[String],
                        setup: Setup,
                        exit: Exit,
                        personalService: PersonalService,
                        control: Control,
                        financialRisk: FinancialRisk,
                        partAndParcel: PartAndParcel,
                        completed: LocalDateTime)

object LogInterview extends DateTimeJsonFormat with FeatureSwitching {

  implicit val writesLocalDateTime: Writes[LocalDateTime] = Writes { date =>
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    JsString(date.format(formatter))
  }
  implicit val logInterviewFormat = Json.format[LogInterview]

  def apply(decisionRequest: Interview, decisionResult: DecisionResponse, dateTimeUtil: DateTimeUtil): LogInterview = {
    LogInterview(
      decisionResult.version,
      "",
      decisionRequest.route,
      decisionResult.result.toString,
      None,
      Setup(
        decisionRequest.endUserRole,
        decisionRequest.hasContractStarted,
        decisionRequest.calculateProvideServices
      ),
      Exit(decisionRequest.officeHolder),
      PersonalService(
        decisionRequest.workerSentActualSubstitute,
        decisionRequest.workerPayActualSubstitute,
        decisionRequest.possibleSubstituteRejection,
        decisionRequest.possibleSubstituteWorkerPay,
        decisionRequest.wouldWorkerPayHelper
      ),
      Control(
        decisionRequest.engagerMovingWorker,
        decisionRequest.workerDecidingHowWorkIsDone,
        decisionRequest.whenWorkHasToBeDone,
        decisionRequest.workerDecideWhere
      ),
      FinancialRisk(
        decisionRequest.workerProvidedMaterials,
        decisionRequest.workerProvidedEquipment,
        decisionRequest.workerUsedVehicle,
        decisionRequest.workerHadOtherExpenses,
        decisionRequest.expensesAreNotRelevantForRole,
        decisionRequest.workerMainIncome,
        decisionRequest.paidForSubstandardWork
      ),
      PartAndParcel(
        decisionRequest.workerReceivesBenefits,
        decisionRequest.workerAsLineManager,
        decisionRequest.contactWithEngagerCustomer,
        decisionRequest.workerRepresentsEngagerBusiness
      ),
      dateTimeUtil.utc
    )
  }
}
