/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.models

import org.joda.time.DateTime


case class LogInterview(version: String, compressedInterview: String, route: String, decision:String, count: Option[String], setup: Setup,
                    exit: Exit, personalService: Option[PersonalService], control: Option[Control], financialRisk: Option[FinancialRisk],
                    partAndParcel: Option[PartAndParcel], completed: DateTime)

case class Setup(endUserRole: String, hasContractStarted: String, provideServices: String)

case class Exit(officeHolder: String)


case class PersonalService(workerSentActualSubstitute: Option[String] = None, workerPayActualSubstitute: Option[String] = None,
                     possibleSubstituteRejection: Option[String] = None, possibleSubstituteWorkerPay: Option[String] = None,
                     wouldWorkerPayHelper: Option[String] = None)


object PersonalService {
  def apply(interview: Map[String, Map[String, String]]): PersonalService = {
    interview.get("personalService").fold[PersonalService]
      {PersonalService()}
      { ps =>
        PersonalService(
          ps.get("workerSentActualSubstitute"),
          ps.get("workerPayActualSubstitute"),
          ps.get("possibleSubstituteRejection"),
          ps.get("possibleSubstituteWorkerPay"),
          ps.get("wouldWorkerPayHelper"))
      }
  }
}

case class Control(engagerMovingWorker: Option[String] = None, workerDecidingHowWorkIsDone: Option[String] = None, workHasToBeDone: Option[String] = None,
                     workerDecideWhere: Option[String] = None)

object Control {
  def apply(interview: Map[String, Map[String, String]]): Control = {
    interview.get("control").fold[Control]
      {Control()}
      { c =>
        Control(
          c.get("engagerMovingWorker"),
          c.get("workerDecidingHowWorkIsDone"),
          c.get("workHasToBeDone"),
          c.get("workerDecideWhere"))
      }
  }
}

case class FinancialRisk(workerProvidedMaterials: Option[String] = None, workerProvidedEquipment: Option[String] = None,
                         workerUsedVehicle: Option[String] = None, workerHadOtherExpenses: Option[String] = None,
                         expensesAreNotRelevantForRole: Option[String] = None,
                         workerMainIncome: Option[String] = None, paidForSubstandardWork: Option[String] = None)

object FinancialRisk {
  def apply(interview: Map[String, Map[String, String]]): FinancialRisk = {
    interview.get("financialRisk").fold[FinancialRisk]
      {FinancialRisk()}
      { fr =>
        FinancialRisk(
          fr.get("workerProvidedMaterials"),
          fr.get("workerProvidedEquipment"),
          fr.get("workerUsedVehicle"),
          fr.get("workerHadOtherExpenses"),
          fr.get("expensesAreNotRelevantForRole"),
          fr.get("workerMainIncome"),
          fr.get("paidForSubstandardWork"))
      }
  }
}

case class PartAndParcel(workerReceivesBenefits: Option[String] = None, workerAsLineManager: Option[String] = None,
                         contactWithEngagerCustomer: Option[String] = None,  workerRepresentsEngagerBusiness: Option[String] = None)

object PartAndParcel {
  def apply(interview: Map[String, Map[String, String]]): PartAndParcel = {
    interview.get("partAndParcel").fold[PartAndParcel]
      {PartAndParcel()}
      { pp =>
        PartAndParcel(
          pp.get("workerReceivesBenefits"),
          pp.get("workerAsLineManager"),
          pp.get("contactWithEngagerCustomer"),
          pp.get("workerRepresentsEngagerBusiness"))
      }
  }
}

case class InterviewSearch(version: String, start: DateTime, end: DateTime)

case class InterviewSearchResponse(compressedInterview: String, route: String, decision: String, count: String, setupEndUserRole: String,
                     setupHasContractStarted: String, setupProvideServices: String,  exitOfficeHolder: String,
                     personalServiceWorkerSentActualSubstitute: String, personalServiceWorkerPayActualSubstitute: String,
                     personalServicePossibleSubstituteRejection: String, personalServicePossibleSubstituteWorkerPay: String,
                     personalServiceWouldWorkerPayHelper: String, controlEngagerMovingWorker: String,
                     controlWorkerDecidingHowWorkIsDone: String, controlWhenWorkHasToBeDone: String,
                     controlWorkerDecideWhere: String, financialRiskHaveToPayButCannotClaim: String,
                     financialRiskWorkerMainIncome: String, financialRiskPaidForSubstandardWork: String,
                     partParcelWorkerReceivesBenefits: String, partParcelWorkerAsLineManager: String,
                     partParcelContactWithEngagerCustomer: String,  partParcelWorkerRepresentsEngagerBusiness: String)

case class AnalyticsResponse(interviews: List[InterviewSearchResponse])




