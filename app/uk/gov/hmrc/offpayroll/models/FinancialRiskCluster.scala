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

import play.api.Logger

object FinancialRiskCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "financialRisk"

  override def clusterID: Int = 2

  val clusterElements: List[Element] = List(
    Element("haveToPayButCannotClaim", GROUP, 0, this,
      List(
        Element("workerProvidedMaterials", CHECKBOX, 0, this),
        Element("workerProvidedEquipment", CHECKBOX, 1, this),
        Element("workerUsedVehicle", CHECKBOX, 2, this),
        Element("workerHadOtherExpenses", CHECKBOX, 3, this),
        Element("expensesAreNotRelevantForRole", CHECKBOX, 4, this)
      ), ElementVerifier.nonEmptyAndExclusive("expensesAreNotRelevantForRole")),
    Element("workerMainIncome", MULTI, 1, this,
      List(
        Element("workerMainIncome.incomeCalendarPeriods", RADIO, 0, this),
        Element("workerMainIncome.incomeFixed", RADIO, 1, this),
        Element("workerMainIncome.incomePieceRate", RADIO, 2, this),
        Element("workerMainIncome.incomeCommission", RADIO, 3, this),
        Element("workerMainIncome.incomeProfitOrLosses", RADIO, 4, this)
      )
    ),
    Element("paidForSubstandardWork", MULTI, 2, this,
      List(
        Element("paidForSubstandardWork.asPartOfUsualRateInWorkingHours", RADIO, 0, this),
        Element("paidForSubstandardWork.outsideOfUsualRateAndHours", RADIO, 1, this),
        Element("paidForSubstandardWork.outsideOfHoursNoCharge", RADIO, 2, this),
        Element("paidForSubstandardWork.noObligationToCorrect", RADIO, 3, this),
        Element("paidForSubstandardWork.cannotBeCorrected", RADIO, 4, this)
      )
    )
  )

  override val flows = List(
    FlowElement("financialRisk.haveToPayButCannotClaim",
      Map("financialRisk.workerProvidedMaterials" -> "Yes"),
      None),
    FlowElement("financialRisk.haveToPayButCannotClaim",
      Map("financialRisk.workerProvidedEquipment" -> "Yes"),
      None),
    FlowElement("financialRisk.haveToPayButCannotClaim",
      Map(
        "financialRisk.workerProvidedMaterials" -> "Yes",
        "financialRisk.workerProvidedEquipment" -> "Yes"),
      None),
    FlowElement("financialRisk.haveToPayButCannotClaim",
      Map(
        "financialRisk.workerProvidedMaterials" -> "Yes",
        "financialRisk.workerProvidedEquipment" -> "Yes",
        "financialRisk.workerUsedVehicle" -> "Yes",
        "financialRisk.workerHadOtherExpenses" -> "Yes"),
      None),
    FlowElement("financialRisk.haveToPayButCannotClaim",
      Map(
        "financialRisk.workerUsedVehicle" -> "Yes",
        "financialRisk.workerHadOtherExpenses" -> "Yes"),
      None)
  )


  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param clusterAnswers
    * @return
    */
  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    Logger.debug(s"AskForDecision: clusterAnswers=$clusterAnswers")
    val normalizedAnswers = DecisionBuilder.decodeMultipleValues(clusterAnswers.toMap)
    Logger.debug(s"AskForDecision: normalizedAnswers=$normalizedAnswers")

    if (allQuestionsAnswered(normalizedAnswers.toList))
      None
    else
      getNextQuestionElement(normalizedAnswers.toList, currentQnA)
  }

  override def getNextQuestionElement(interview: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    val relevantFlowElement = flows.filter { element =>
      Logger.debug(s"element.answer: ${element.answers}")
      element.answers.forall(interview.contains(_))
    }
    if (relevantFlowElement.isEmpty)
      findNextQuestion(currentQnA)
    else
      getElementForQuestionTag(relevantFlowElement.head.nextQuestion.getOrElse(""))
  }


}