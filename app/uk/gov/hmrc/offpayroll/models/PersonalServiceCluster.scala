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
object PersonalServiceCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "personalService"

  override def clusterID: Int = 0

  val clusterElements: List[Element] = List(
    Element("workerSentActualSubstitute", MULTI, 0, this,
      List(
        Element("workerSentActualSubstitute.yesClientAgreed", RADIO, 0, this),
        Element("workerSentActualSubstitute.notAgreedWithClient", RADIO, 1, this),
        Element("workerSentActualSubstitute.noSubstitutionHappened", RADIO, 2, this)
      )
    ),
    Element("workerPayActualSubstitute", RADIO, 1, this),
    Element("possibleSubstituteRejection", MULTI, 2, this,
      List(
        Element("possibleSubstituteRejection.wouldReject", RADIO, 0, this),
        Element("possibleSubstituteRejection.wouldNotReject", RADIO, 1, this)
      )
    ),
    Element("possibleSubstituteWorkerPay", RADIO, 3, this),
    Element("wouldWorkerPayHelper", RADIO, 4, this)
  )

  override val flows = List(
    FlowElement("personalService.workerSentActualSubstitute",
      Map("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.notAgreedWithClient"),
      Option("personalService.wouldWorkerPayHelper")),
    FlowElement("personalService.workerSentActualSubstitute",
      Map("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened"),
      Option("personalService.possibleSubstituteRejection")),
    FlowElement("personalService.workerPayActualSubstitute",
      Map("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.yesClientAgreed",
        "personalService.workerPayActualSubstitute" -> "Yes"),
      Option.empty),
    FlowElement("personalService.workerPayActualSubstitute",
      Map("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.yesClientAgreed",
        "personalService.workerPayActualSubstitute" -> "No"),
      Option("personalService.wouldWorkerPayHelper")),
    FlowElement("personalService.possibleSubstituteRejection",
      Map("setup.hasContractStarted" -> "Yes",
        "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject"),
      Option("personalService.wouldWorkerPayHelper")),
    FlowElement("personalService.possibleSubstituteRejection",
      Map("setup.hasContractStarted" -> "No",
        "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject"),
      Option.empty),
    FlowElement("personalService.possibleSubstituteWorkerPay",
      Map("setup.hasContractStarted" -> "Yes",
        "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
        "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject",
        "personalService.possibleSubstituteWorkerPay" -> "Yes"),
      Option.empty),
    FlowElement("personalService.possibleSubstituteWorkerPay",
      Map("setup.hasContractStarted" -> "No",
        "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject",
        "personalService.possibleSubstituteWorkerPay" -> "No"),
      Option.empty),
    FlowElement("personalService.possibleSubstituteWorkerPay",
      Map("setup.hasContractStarted" -> "No",
        "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject",
        "personalService.possibleSubstituteWorkerPay" -> "Yes"),
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
      getNextQuestionElement(interview, currentQnA)

  }

  override def getStart(interview: Map[String, String]): Option[Element] =
    interview.find { case (q, _) => q == "setup.hasContractStarted" }.flatMap { case (q, a) =>
      if (a.toUpperCase == "NO") clusterElements.find {
        _.questionTag == name + ".possibleSubstituteRejection"
      }
      else clusterElements.headOption
    }

}
