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

object FinancialRiskBCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "financialRiskB"

  override def clusterID: Int = 3

  val clusterElements: List[Element] = List(
    Element("provideVehicle", RADIO, 0, this),
    Element("engagerPayVehicleExpense", RADIO, 1, this),
    Element("workerIncurExpense", RADIO, 2, this),
    Element("engagerPayOtherExpense", RADIO, 3, this),
    Element("workerMainIncome", MULTI, 4, this,
      List(
        Element("workerMainIncome.incomeCalendarPeriods", RADIO, 0, this),
        Element("workerMainIncome.incomePieceRate", RADIO, 1, this),
        Element("workerMainIncome.incomeCommission", RADIO, 2, this),
        Element("workerMainIncome.incomeFixed", RADIO, 3, this),
        Element("workerMainIncome.incomeProfitOrLosses", RADIO, 4, this),
        Element("workerMainIncome.incomeRateByInvoice", RADIO, 5, this)
      )
    ),
    Element("workerSufferedLatePayment", RADIO, 5, this),
    Element("workerProvideAtTheirExpense", RADIO, 6, this),
    Element("workerFixAtTheirOwnTime", RADIO, 7, this),
    Element("workerPayForMaterialsSubstandardWorker", RADIO, 8, this),
    Element("paidForSubstandardWork", RADIO, 9, this),
    Element("actualEngagerWithholdPayment", RADIO, 10, this)
  )

  private val flows = List(
    FlowElement("financialRiskB.provideVehicle",
      Map("financialRiskB.provideVehicle" -> "No"),
      Option("financialRiskB.workerIncurExpense")),
    FlowElement("financialRiskB.workerIncurExpense",
      Map("financialRiskB.workerIncurExpense" -> "No"),
      Option("financialRiskB.workerMainIncome")),
    FlowElement("financialRiskB.workerMainIncome",
      Map("financialRiskB.workerMainIncome" -> "financialRiskB.workerMainIncome.incomeCalendarPeriods"),
      Option("financialRiskB.workerProvideAtTheirExpense")),
    FlowElement("financialRiskB.workerMainIncome",
      Map("financialRiskB.workerMainIncome" -> "financialRiskB.workerMainIncome.incomePieceRate"),
      Option("financialRiskB.workerProvideAtTheirExpense")),
    FlowElement("financialRiskB.workerMainIncome",
      Map("financialRiskB.workerMainIncome" -> "financialRiskB.workerMainIncome.incomeCommission"),
      Option("financialRiskB.workerProvideAtTheirExpense")),
    FlowElement("financialRiskB.workerMainIncome",
      Map("financialRiskB.workerMainIncome" -> "financialRiskB.workerMainIncome.incomeFixed"),
      Option("financialRiskB.workerProvideAtTheirExpense")),
    FlowElement("financialRiskB.workerMainIncome",
      Map("financialRiskB.workerMainIncome" -> "financialRiskB.workerMainIncome.incomeProfitOrLosses"),
      Option("financialRiskB.workerProvideAtTheirExpense")),
    FlowElement("financialRiskB.workerProvideAtTheirExpense",
      Map("financialRiskB.workerProvideAtTheirExpense" -> "No"),
      Option("financialRiskB.paidForSubstandardWork")),
    FlowElement("financialRiskB.workerFixAtTheirOwnTime",
      Map("financialRiskB.workerFixAtTheirOwnTime" -> "Yes"),
      Option("financialRiskB.paidForSubstandardWork")),
    FlowElement("financialRiskB.paidForSubstandardWork",
      Map("financialRiskB.paidForSubstandardWork" -> "No"),
      Option.empty)

  )

  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param clusterAnswers
    * @return
    */
  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if (allQuestionsAnswered(clusterAnswers))Option.empty
    else
      getNextQuestionElement(clusterAnswers, currentQnA)

  }

  def getNextQuestionElement(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    val currentQuestionFlowElements = flows.filter(_.currentQuestion.equalsIgnoreCase(currentQnA._1))
    val relevantFlowElement = currentQuestionFlowElements.filter{
      element => element.answers.forall(clusterAnswers.contains(_))
    }
    if(relevantFlowElement.isEmpty) findNextQuestion(currentQnA)
    else
      getElementForQuestionTag(relevantFlowElement.head.nextQuestion.getOrElse(""))
  }

}