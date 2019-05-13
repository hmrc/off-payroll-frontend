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

import models.WorkerType.SoleTrader
import models.{DecisionResponse, Interview}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._
import utils.DateTimeJsonFormat

case class LogInterview(version: String, compressedInterview: String, route: String, decision:String, count: Option[String], setup: Setup,
                     exit: Exit, personalService: Option[PersonalService], control: Option[Control], financialRisk: Option[FinancialRisk],
                     partAndParcel: Option[PartAndParcel], completed: DateTime)

object LogInterview extends DateTimeJsonFormat {

  implicit val logInterviewFormat = Json.format[LogInterview]

  def createFromInterview(decisionRequest: Interview, decisionResult: DecisionResponse): LogInterview = {
    LogInterview(
      decisionResult.version,
      "",
      decisionRequest.provideServices match {
        case Some(workerType) if workerType == SoleTrader => "ESI"
        case _ => "IR35"
      },
      decisionResult.result.toString,
      None,
      Setup(
        decisionRequest.endUserRole.get.toString,
        booleanToYesNo(decisionRequest.hasContractStarted.get),
        decisionRequest.provideServices.get.toString
      ),
      Exit(booleanToYesNo(decisionRequest.officeHolder.get)),
      Some(PersonalService(
        decisionRequest.workerSentActualSubstitute.fold(None: Option[String]){sub => Some(sub.toString)},
        decisionRequest.workerPayActualSubstitute.fold(None: Option[String]){pay => Some(pay.toString)},
        decisionRequest.possibleSubstituteRejection.fold(None: Option[String]){reject => Some(reject.toString)},
        decisionRequest.possibleSubstituteWorkerPay.fold(None: Option[String]){possible => Some(possible.toString)},
        decisionRequest.wouldWorkerPayHelper.fold(None: Option[String]){helper => Some(helper.toString)}
      )),
      Some(Control(decisionRequest.engagerMovingWorker.fold(None: Option[String]){move => Some(move.toString)},
        decisionRequest.workerDecidingHowWorkIsDone.fold(None: Option[String]){decide => Some(decide.toString)},
        decisionRequest.whenWorkHasToBeDone.fold(None: Option[String]){done => Some(done.toString)},
        decisionRequest.workerDecideWhere.fold(None: Option[String]){where => Some(where.toString)}
      )),
      Some(FinancialRisk(
        decisionRequest.workerProvidedMaterials.fold(None: Option[String]){material => Some(booleanToYesNo(material))},
        decisionRequest.workerProvidedEquipment.fold(None: Option[String]){equip => Some(booleanToYesNo(equip))},
        decisionRequest.workerUsedVehicle.fold(None: Option[String]){vehicle => Some(booleanToYesNo(vehicle))},
        decisionRequest.workerHadOtherExpenses.fold(None: Option[String]){expense => Some(booleanToYesNo(expense))},
        decisionRequest.expensesAreNotRelevantForRole.fold(None: Option[String]){relevant => Some(booleanToYesNo(relevant))},
        decisionRequest.workerMainIncome.fold(None: Option[String]){income => Some(income.toString)},
        decisionRequest.paidForSubstandardWork.fold(None: Option[String]){paid => Some(paid.toString)}
      )),
      Some(PartAndParcel(
        decisionRequest.workerReceivesBenefits.fold(None: Option[String]){benefit => Some(booleanToYesNo(benefit))},
        decisionRequest.workerAsLineManager.fold(None: Option[String]){manager => Some(booleanToYesNo(manager))},
        decisionRequest.contactWithEngagerCustomer.fold(None: Option[String]){customer => Some(booleanToYesNo(customer))},
        decisionRequest.workerRepresentsEngagerBusiness.fold(None: Option[String]){business => Some(business.toString)}
      )),
      new DateTime(DateTimeZone.UTC)
    )
  }

  private def booleanToYesNo(value: Boolean): String =
    if (value) "Yes" else "No"
}
