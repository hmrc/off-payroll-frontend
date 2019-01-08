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

package uk.gov.hmrc.offpayroll.util

import play.twirl.api.Html
import uk.gov.hmrc.offpayroll.models._

/**
  * Created by Habeeb on 05/05/2017.
  */
class ResultPageHelper(val interview: List[(String, List[String])], decision: DecisionType, val fragments: Map[String, Html], decisionCluster: String, esi: Boolean) {

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

  lazy val decisionKey : String = {
    val decisionTag =
      if(decisionCluster.equals(PersonalServiceCluster.name) && !esi)
        getClusterLabel(decisionCluster) + DOT + getFutureOrCurrent + getDecisionString
      else getClusterLabel(decisionCluster) + getDecisionString

    if(!decision.equals(UNKNOWN)) decisionTag + esiOrIr35 else decisionTag
  }

  def getQuestionsAndAnswersForCluster(clusterName: String): List[(String, List[String])] = {
    interview.filter{qAndA =>
      qAndA._1.startsWith(clusterName)
    }
  }

  def decisionUnknown : Boolean = decision.equals(UNKNOWN)

  private def esiOrIr35: String = if(esi) s"${DOT}esi" else s"${DOT}ir35"

  private def getFutureOrCurrent : String = {
    val exists = interview.exists {
      case (question, answer) => "setup.hasContractStarted" == question && "YES" == answer.head.toUpperCase
    }
    if(exists) "current" else "future"
  }

  private def getClusterLabel(clusterName: String): String = {
    if(esi)
       CLUSTER_TO_LABEL.get(clusterName).get._2
    else CLUSTER_TO_LABEL.get(clusterName).get._1
  }

  private def getDecisionString: String = {
      DOT + decision.toString.toLowerCase
  }

}

object ResultPageHelper{
  def apply(interview: List[(String, List[String])], decision: DecisionType, fragments: Map[String, Html], decisionCluster: String, esi: Boolean): ResultPageHelper = {
    new ResultPageHelper(interview, decision, fragments, decisionCluster, esi)
  }
}
