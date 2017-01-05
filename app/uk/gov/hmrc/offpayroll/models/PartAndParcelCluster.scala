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

/**
  * Created by peter on 15/12/2016.
  */
object PartAndParcelCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "partParcel"

  override def clusterID: Int = 0

  val clusterElements: List[Element] = List(
    Element("workerReceivesBenefits", RADIO, 0, this),
    Element("workerAsLineManager", RADIO, 1, this),
    Element("contactWithEngagerCustomer", RADIO, 2, this),
    Element("workerRepresentsEngagerBusiness", RADIO, 3, this)
  )

  private val flows = List(
    FlowElement("partParcel.workerReceivesBenefits", Map("partParcel.workerReceivesBenefits" -> "No"), Option("partParcel.workerAsLineManager")),
    FlowElement("partParcel.workerReceivesBenefits", Map("partParcel.workerReceivesBenefits" -> "Yes"), Option.empty),
    FlowElement("partParcel.workerAsLineManager", Map("partParcel.workerReceivesBenefits" -> "No", "partParcel.workerAsLineManager" -> "Yes"), Option.empty),
    FlowElement("partParcel.workerAsLineManager", Map("partParcel.workerReceivesBenefits" -> "No", "partParcel.workerAsLineManager" -> "No"), Option("partParcel.contactWithEngagerCustomer")),
    FlowElement("partParcel.contactWithEngagerCustomer", Map("partParcel.workerReceivesBenefits" -> "No", "partParcel.workerAsLineManager" -> "No", "partParcel.contactWithEngagerCustomer" -> "Yes"), Option("partParcel.workerRepresentsEngagerBusiness")),
    FlowElement("partParcel.contactWithEngagerCustomer", Map("partParcel.workerReceivesBenefits" -> "No", "partParcel.workerAsLineManager" -> "No", "partParcel.contactWithEngagerCustomer" -> "No"), Option.empty)
  )

  /**
    * Returns the next element in the cluster or empty if we should ask for a decision
    *
    * @param clusterAnswers
    * @return
    */
  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if (clusterElements.forall((element) => clusterAnswers.exists(a => a._1 == element.questionTag))) {
      Option.empty
    } else {
      val maybeString = getNextQuestionTag(clusterAnswers, currentQnA)
      if(maybeString.isEmpty){
        Option.empty
      }else
      clusterElements.find(element => element.questionTag.equalsIgnoreCase(maybeString.get))
    }

  }

  def getNextQuestionTag(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[String] = {
    val currentQuestionFlowElements = flows.filter(_.currentQuestion.equalsIgnoreCase(currentQnA._1))
    val relevantFlowElement = currentQuestionFlowElements.filter(_.answers.toList.equals(clusterAnswers))
    if(relevantFlowElement.size == 0){
      Option.empty
    }else
      relevantFlowElement.head.nextQuestion
  }
}

case class FlowElement(currentQuestion:String, answers:Map[String, String], nextQuestion:Option[String])