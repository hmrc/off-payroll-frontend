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

object BusinessStructureCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "businessStructure"

  override def clusterID: Int = 5

  val clusterElements: List[Element] = List(
    Element("similarWork", MULTI, 0, this,
      List(
        Element("similarWork.zeroToThree", RADIO, 0, this),
        Element("similarWork.fourToNine", RADIO, 1, this),
        Element("similarWork.tenPlus", RADIO, 2, this)
      )
    ),
    Element("workerVAT", RADIO, 1, this),
    Element("businessAccount", RADIO, 2, this),
    Element("advertiseForWork", RADIO, 3, this),
    Element("businessWebsite", RADIO, 4, this),
    Element("workerPayForTraining", RADIO, 5, this),
    Element("workerExpenseRunningBusinessPremises", RADIO, 6, this),
    Element("workerPaysForInsurance", RADIO, 7, this)
  )

  override val flows = List(
    FlowElement("businessStructure.similarWork",
      Map("businessStructure.similarWork" -> "businessStructure.similarWork.zeroToThree"),
      Option.empty),
    FlowElement("businessStructure.similarWork",
      Map("businessStructure.similarWork" -> "businessStructure.similarWork.tenPlus"),
      Option.empty)
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
      getNextQuestionElement(clusterAnswers, currentQnA)
  }

}