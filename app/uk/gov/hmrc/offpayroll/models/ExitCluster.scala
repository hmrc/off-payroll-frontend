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

import play.Logger

/**
  * Created by peter on 08/01/2017.
  */
object ExitCluster extends Cluster {

  private val conditionsliabilityltd = "conditionsLiabilityLtd"
  private val officeholder = "officeHolder"
  private val conditionsliabilitypartnership = "conditionsLiabilityPartnership"
  private val conditionsliabilityindividualintermediary = "conditionsLiabilityIndividualIntermediary"
  private val setup_provideServices = "setup.provideServices"

  val YES = "YES"

  val DUMMY = Element("", RADIO, 0, this)

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "exit"

  /**
    * All the Elements that make up this cluster
    *
    * @return
    */
  override def clusterElements: List[Element] =
    List(
      Element(officeholder, RADIO, 0, this),
      Element(conditionsliabilityltd + "1", RADIO, 1, this),
      Element(conditionsliabilityltd + "2", RADIO, 2, this),
      Element(conditionsliabilityltd + "3", RADIO, 3, this),
      Element(conditionsliabilityltd + "4", RADIO, 4, this),
      Element(conditionsliabilityltd + "5", RADIO, 5, this),
      Element(conditionsliabilityltd + "6", RADIO, 6, this),
      Element(conditionsliabilityltd + "7", RADIO, 7, this),
      Element(conditionsliabilityltd + "8", RADIO, 8, this),
      Element(conditionsliabilitypartnership + "1", RADIO, 9, this),
      Element(conditionsliabilitypartnership + "2", RADIO, 10, this),
      Element(conditionsliabilitypartnership + "3", RADIO, 11, this),
      Element(conditionsliabilitypartnership + "4", RADIO, 12, this),
      Element(conditionsliabilityindividualintermediary, RADIO, 13, this)
    )

  /**
    * Helps order a Cluster in an Interview
    *
    * @return
    */
  override def clusterID: Int = 1


  override def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element] = {
    def checkForAnswerInInterview(question: String, answer: String) = {
      clusterAnswers.exists {
        case (q, a) => q == question && a.toUpperCase == answer.toUpperCase
      }
    }

    def startsWith(element: Element, prefix: String) = {
      element._questionTag.startsWith(prefix)
    }

    def isOfficeHolder(element: Element) = {
      element._questionTag == officeholder
    }

    def filterClusterElementsAndReOrder(tagPrefix: String) = {
      clusterElements.filter(e => startsWith(e, tagPrefix) || isOfficeHolder(e))
    }


    def anyAnswersYes: Boolean = {
      elements.exists{
        case (element) => clusterAnswers.exists{
          case (question, answer) => element.questionTag == question && answer.toUpperCase == YES
        }
      }
    }

    def findNextQuestionBasedOnListOrder: Option[Element] = {
      val indexOfElement: Int = elements.indexOf(
        elements.find(element => element.questionTag == currentQnA._1).getOrElse(DUMMY))
      if(indexOfElement + 1 < elements.size) Option(elements(indexOfElement +1))
      else Option.empty
    }

    lazy val elements: List[Element] =
      if (checkForAnswerInInterview(setup_provideServices, setup_provideServices + ".limitedCompany")) {
        filterClusterElementsAndReOrder(conditionsliabilityltd)
      } else if (checkForAnswerInInterview(setup_provideServices, setup_provideServices + ".partnership")) {
        filterClusterElementsAndReOrder(conditionsliabilitypartnership)
      } else if (checkForAnswerInInterview(setup_provideServices, setup_provideServices + ".intermediary")) {
        filterClusterElementsAndReOrder(conditionsliabilityindividualintermediary)
      } else List()


    val officeHolder: Boolean = checkForAnswerInInterview(name + "." + officeholder, YES)

    if(officeHolder || anyAnswersYes) Option.empty
    else findNextQuestionBasedOnListOrder

  }

}
