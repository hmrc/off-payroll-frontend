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
  * Created by peter on 08/01/2017.
  */
object ExitCluster extends Cluster {


  private val LTD_QUESTION_1 = clusterElements(1)
  private val PARTNERSHIP_QUESTION_1 = clusterElements(9)
  private val INTERMEDIARY_QUESTION = clusterElements(13)

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
      Element("officeHolder", RADIO, 0, this),
      Element("conditionsLiabilityLtd1", RADIO, 1, this),
      Element("conditionsLiabilityLtd2", RADIO, 2, this),
      Element("conditionsLiabilityLtd3", RADIO, 3, this),
      Element("conditionsLiabilityLtd4", RADIO, 4, this),
      Element("conditionsLiabilityLtd5", RADIO, 5, this),
      Element("conditionsLiabilityLtd6", RADIO, 6, this),
      Element("conditionsLiabilityLtd7", RADIO, 7, this),
      Element("conditionsLiabilityLtd8", RADIO, 8, this),
      Element("conditionsLiabilityPartnership1", RADIO, 9, this),
      Element("conditionsLiabilityPartnership2", RADIO, 10, this),
      Element("conditionsLiabilityPartnership3", RADIO, 11, this),
      Element("conditionsLiabilityPartnership4", RADIO, 12, this),
      Element("conditionsLiabilityIndividualIntermediary", RADIO, 13, this)
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
        case (q, a) => q == question && a.toUpperCase == answer
      }
    }

    if (checkForAnswerInInterview(name + ".officeHolder", "YES")) Option.empty
    else if (checkForAnswerInInterview("setup.provideServices.limitedCompany", "YES")) Option(LTD_QUESTION_1)
    else if (checkForAnswerInInterview("setup.provideServices.partnership", "YES")) Option(PARTNERSHIP_QUESTION_1)
    else if (checkForAnswerInInterview("setup.provideServices.intermediary", "YES")) Option(INTERMEDIARY_QUESTION)
    else Option.empty

  }

}
