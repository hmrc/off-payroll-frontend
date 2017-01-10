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
  * Represents a Cluster which is a part of an Interview in Offpayroll
  */
abstract class Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  def name: String

  /**
    * All the Elements that make up this cluster
    *
    * @return
    */
  def clusterElements: List[Element]


  def makeMapFromClusterElements: Map[String, Element] = {
    Map() ++ (clusterElements map { element => (element.questionTag, element) })
  }

  def findNextQuestion(currentQnA: (String, String)):Option[Element] = currentQnA match {
    case (element, answer) => {
      val currentElement = clusterElements.find(e => {
        if(e.questionTag == element) e.questionTag == element
        else if(e.children == Nil) e.questionTag == element
        else e.children.exists(e2 => e2.questionTag == element)
      })
      if(currentElement.nonEmpty) {
        clusterElements.find(e => e.order == currentElement.get.order + 1)
      }
      else Option.empty
    }
  }

  def allQuestionsAnswered(clusterAnswers: List[(String, String)]):Boolean = {

    clusterElements.forall(element => {
      clusterAnswers.exists{
        case (question, answer) => {
          if(element.children != Nil) {
            element.children.exists(e => e.questionTag == question)
          } else element.questionTag == question
        }
      }
    })
  }

  def getElementForQuestionTag(questionTag : String):Option[Element] ={
    clusterElements.find(element => element.questionTag.equalsIgnoreCase(questionTag))
  }

  /**
    * Helps order a Cluster in an Interview
    *
    * @return
    */
  def clusterID: Int

  /**
    *
    * Based on what has been answered for this cluster should we ask
    * for a Decision
    *
    * @param clusterAnswers
    * @return
    */
  def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    if(allQuestionsAnswered(clusterAnswers)) Option.empty
    else findNextQuestion(currentQnA)
  }


  override def toString: String = {
    "{Cluster ID: " + clusterID + " Name: " + name + "}"
  }

}

case class FlowElement(currentQuestion: String, answers: Map[String, String], nextQuestion: Option[String])