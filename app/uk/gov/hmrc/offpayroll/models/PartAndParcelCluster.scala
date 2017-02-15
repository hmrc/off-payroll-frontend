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

object PartAndParcelCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "partParcel"

  override def clusterID: Int = 3


  val clusterElements: List[Element] = List(
    Element("workerReceivesBenefits", RADIO, 0, this),
    Element("workerAsLineManager", RADIO, 1, this),
    Element("contactWithEngagerCustomer", RADIO, 2, this),
    Element("workerRepresentsEngagerBusiness",MULTI, 3, this,
      List(
        Element("workerRepresentsEngagerBusiness.workForEndClient", RADIO, 0, this),
        Element("workerRepresentsEngagerBusiness.workAsIndependent", RADIO, 1, this),
        Element("workerRepresentsEngagerBusiness.workAsBusiness", RADIO, 2, this)
      ))
  )

  private val flows = List(
    FlowElement("partParcel.workerReceivesBenefits",
      Map("partParcel.workerReceivesBenefits" -> "Yes"),
      Option("partParcel.contactWithEngagerCustomer")),
    FlowElement("partParcel.workerAsLineManager",
      Map("partParcel.workerAsLineManager" -> "Yes"),
      Option.empty),
    FlowElement("partParcel.contactWithEngagerCustomer",
      Map("partParcel.contactWithEngagerCustomer" -> "No"),
      Option.empty)
  )

  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param interview
    * @return
    */
  override def shouldAskForDecision(interview: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if (allQuestionsAnswered(interview))Option.empty
    else
      getNextQuestionTag(interview, currentQnA)

  }

  def getNextQuestionTag(interview: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    val currentQuestionFlowElements = flows.filter(_.currentQuestion.equalsIgnoreCase(currentQnA._1))
    val relevantFlowElement = currentQuestionFlowElements.filter{
      element => element.answers.forall(interview.contains(_))
    }
    if(relevantFlowElement.isEmpty) findNextQuestion(currentQnA)
    else
      getElementForQuestionTag(relevantFlowElement.head.nextQuestion.getOrElse(""))
  }
}