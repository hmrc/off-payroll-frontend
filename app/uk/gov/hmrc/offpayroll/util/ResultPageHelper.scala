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

package uk.gov.hmrc.offpayroll.util

import uk.gov.hmrc.offpayroll.models._

/**
  * Created by Habeeb on 05/05/2017.
  */
class ResultPageHelper(interview: List[(String, List[String])], decision: DecisionType) {

  private val DOT = "."
  private val MATRIX = "matrix"
  private val EARLY_EXIT = "earlyExit"
  private val CLUSTER_TO_LABEL: Map[String,(String, String)]  = Map(
    ExitCluster.name -> ("officeHolder", "officeHolder"),
    FinancialRiskCluster.name -> ("financialRiskCluster", EARLY_EXIT),
    ControlCluster.name -> ("controlCluster", EARLY_EXIT),
    PersonalServiceCluster.name -> ("personalServiceCluster", EARLY_EXIT),
    PartAndParcelCluster.name -> (MATRIX,MATRIX)
  )

  def getQuestionsAndAnswersForCluster(clusterName: String): List[(String, List[String])] = {
    var clusterQuestionAndAnswers: List[(String, List[String])] = List()
    interview.foreach{qAndA =>
      if (qAndA._1.startsWith(clusterName)) clusterQuestionAndAnswers = clusterQuestionAndAnswers :+ qAndA
    }
    clusterQuestionAndAnswers
  }

  def getInterview: List[(String, List[String])] = interview

  def getDecisionTag : String = {
    val lastCompletedCluster = getLastCompletedCluster
    var decisionTag = lastCompletedCluster + getDecisionString
    if(!decision.equals(UNKNOWN)) decisionTag = decisionTag + esiOrIr35
    decisionTag
  }

  private def esiOrIr35: String = {
    val provideServices = interview.toMap.get("setup.provideServices")

    if(!provideServices.isDefined) throw new IllegalStateException("Invalid Interview object passed")

    if(provideServices.get.contains("setup.provideServices.soleTrader")) s"${DOT}esi" else s"${DOT}ir35"
  }

  private def getFutureOrCurrent : String = {
    val hasContractStarted = interview.toMap.get("setup.hasContractStarted")

    if(!hasContractStarted.isDefined) throw new IllegalStateException("Invalid Interview object passed")

    if(hasContractStarted.get.head.toUpperCase.equals("YES")) "current" else "future"

  }

  private def getLastCompletedCluster: String = {
    if(!getQuestionsAndAnswersForCluster(PartAndParcelCluster.name).isEmpty){
      getClusterLabel(PartAndParcelCluster.name, esiOrIr35)
    }
    else if(!getQuestionsAndAnswersForCluster(FinancialRiskCluster.name).isEmpty){
      getClusterLabel(FinancialRiskCluster.name, esiOrIr35)
    }
    else if(!getQuestionsAndAnswersForCluster(ControlCluster.name).isEmpty){
      getClusterLabel(ControlCluster.name, esiOrIr35)
    }
    else if(!getQuestionsAndAnswersForCluster(PersonalServiceCluster.name).isEmpty){
      var label = getClusterLabel(PersonalServiceCluster.name, esiOrIr35)
      if(esiOrIr35.contains("ir35")){
        label = label + DOT + getFutureOrCurrent
      }
      label
    }
    else
      getClusterLabel(ExitCluster.name, esiOrIr35)
  }

  private def getClusterLabel(clusterName: String, esiOrIr35: String): String = {
    if(esiOrIr35.contains("ir35"))
      CLUSTER_TO_LABEL.get(clusterName).get._1
    else CLUSTER_TO_LABEL.get(clusterName).get._2
  }

  private def getDecisionString: String = {
      DOT + decision.toString.toLowerCase
  }

}

object ResultPageHelper{
  def apply(interview: List[(String, List[String])], decision: DecisionType): ResultPageHelper = {
    new ResultPageHelper(interview, decision)
  }
}
