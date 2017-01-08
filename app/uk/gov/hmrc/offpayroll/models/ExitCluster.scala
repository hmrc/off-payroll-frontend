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
      Element("conditionsLiabilityLtd", MULTI, 1, this,
        List(
          Element("conditionsLiabilityLtd.moreThanFivePercent", RADIO, 0, this),
          Element("conditionsLiabilityLtd.familyMoreThanFivePercent", RADIO, 1, this),
          Element("conditionsLiabilityLtd.dividendsMoreThanFivePerent", RADIO, 2, this),
          Element("conditionsLiabilityLtd.familyDividendsMoreThanFivePerent", RADIO, 3, this),
          Element("conditionsLiabilityLtd.controlledLessThanFive", RADIO, 4, this),
          Element("conditionsLiabilityLtd.controlAllDirectors", RADIO, 5, this),
          Element("conditionsLiabilityLtd.woundUpMoreThanFivePercent", RADIO, 6, this),
          Element("conditionsLiabilityLtd.familyWoundUpMoreThanFivePercent", RADIO, 7, this)
        )),
      Element("conditionsLiabilityPartnership", MULTI, 2, this,
        List(
          Element("conditionsLiabilityPartnership.moreThanSixtyPercent", RADIO, 0, this),
          Element("conditionsLiabilityPartnership.familyMoreThanSixtyPercent", RADIO, 0, this),
          Element("conditionsLiabilityPartnership.singleClientMorThanFiftyPercent", RADIO, 0, this),
          Element("conditionsLiabilityPartnership.shareBasedOnProfits", RADIO, 0, this)
        )),
      Element("conditionsLiabilityIndividualIntermediary", RADIO, 3, this)
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

    if(checkForAnswerInInterview(name + ".officeHolder", "YES")) Option.empty
    else if(checkForAnswerInInterview("setup.provideServices.limitedCompany", "YES")) Option(clusterElements(1))
    else if(checkForAnswerInInterview("setup.provideServices.partnership", "YES")) Option(clusterElements(2))
    else if(checkForAnswerInInterview("setup.provideServices.intermediary", "YES")) Option(clusterElements(3))
    else Option.empty

  }

}
