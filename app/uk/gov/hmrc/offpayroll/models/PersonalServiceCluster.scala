/*
 * Copyright 2016 HM Revenue & Customs
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
object PersonalServiceCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "personalService"

  override def clusterID: Int = 0

  val clusterElements: List[Element] = List(
    Element("workerSentActualSubstitiute", MULTI, 0, this, List(
      Element("workerSentActualSubstitiute_1", RADIO, 0, this),
      Element("workerSentActualSubstitiute_2", RADIO, 0, this)
    )
    ),
    Element("contractrualObligationForSubstitute", RADIO, 1, this),
    Element("possibleSubstituteRejection", RADIO, 2, this),
    Element("contractualObligationInPractise", RADIO, 3, this),
    Element("workerPayActualHelper", RADIO, 4, this),
    Element("engagerArrangeWorker", RADIO, 5, this),
    Element("contractTermsWorkerPaysSubstitute", RADIO, 6, this),
    Element("workerSentActualHelper", RADIO, 7, this),
    Element("possibleHelper", RADIO, 8, this)
  )

  override def shouldAskForDecision(clusterAnswers: List[(String, String)]): Boolean = {
    clusterElements.forall((element) => clusterAnswers.exists(a => a._1 == element.questionTag))
  }
}
