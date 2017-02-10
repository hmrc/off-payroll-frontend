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

object FinancialRiskCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "financialRisk"

  override def clusterID: Int = 0 // todo make this 2

  val clusterElements: List[Element] = List(
    Element("haveToPayButCannotClaim", GROUP, 0, this,
      List(
        Element("workerProvidedMaterials", CHECKBOX, 0, this),
        Element("workerProvidedEquipment", CHECKBOX, 1, this),
        Element("workerUsedVehicle", CHECKBOX, 2, this),
        Element("workerHadOtherExpenses", CHECKBOX, 3, this),
        Element("expensesAreNotRelevantForRole", CHECKBOX, 4, this)
      )),
    Element("workerMainIncome", MULTI, 1, this,
      List(
        Element("workerMainIncome.incomeCalendarPeriods", RADIO, 0, this),
        Element("workerMainIncome.incomePieceRate", RADIO, 1, this),
        Element("workerMainIncome.incomeCommission", RADIO, 2, this),
        Element("workerMainIncome.incomeFixed", RADIO, 3, this),
        Element("workerMainIncome.incomeProfitOrLosses", RADIO, 4, this),
        Element("workerMainIncome.incomeRateByInvoice", RADIO, 5, this)
      )
    ),
    Element("paidForSubstandardWork", MULTI, 2, this,
      List(
        Element("paidForSubstandardWork.incomeCalendarPeriods", RADIO, 0, this),
        Element("paidForSubstandardWork.incomePieceRate", RADIO, 1, this),
        Element("paidForSubstandardWork.incomeCommission", RADIO, 2, this),
        Element("paidForSubstandardWork.incomeFixed", RADIO, 3, this),
        Element("paidForSubstandardWork.incomeProfitOrLosses", RADIO, 4, this),
        Element("paidForSubstandardWork.incomeRateByInvoice", RADIO, 5, this)
      )
    )
  )


  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param clusterAnswers
    * @return
    */
  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if (allQuestionsAnswered(clusterAnswers)) Option.empty
    else
      findNextQuestion(currentQnA)

  }


}