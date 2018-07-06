/*
 * Copyright 2018 HM Revenue & Customs
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

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader
import uk.gov.hmrc.offpayroll.resources._



class PersonalServiceClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper{

  private val personalServiceCluster = PersonalServiceCluster

  "The Personal Service Cluster" should " have the correct name " in {
    personalServiceCluster.name shouldBe "personalService"
  }

  it should " have the correct clusterId " in {
    personalServiceCluster.clusterID shouldBe 2
  }

  it should " have the correct amount of question tags " in {
    personalServiceCluster.clusterElements.size shouldBe 5
  }

  it should " have the correct set of questions" in {
    assertAllElementsPresentForCluster(personalServiceCluster) shouldBe true
  }

  it should "ask the next question if not all questions have been asked" in {

    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "Yes"

    val partialAnswers = List(
      ("personalService.workerSentActualSubstitute", "personalService.workerSentActualSubstitute.yesClientAgreed"),
      ("personalService.workerPayActualSubstitute", "Yes"),
      ("personalService.possibleSubstituteRejection", "personalService.possibleSubstituteRejection.wouldReject"),
      currentQnA)

    val decision = personalServiceCluster.shouldAskForDecision(partialAnswers, currentQnA)

    decision.nonEmpty shouldBe true
  }


  it should " not ask anymore questions when all questions have been asked" in {

    val currentQnA = personalService_workerSentActualSubstituteYesClientAgreed
    val fullanswers = PropertyFileLoader.transformMapToAListOfAnswers(PropertyFileLoader.getMessagesForACluster("personalService"))
    val decision = personalServiceCluster.shouldAskForDecision(fullanswers, currentQnA)

    decision.nonEmpty shouldBe false
  }


  // CURRENT FLOW
  // added "setup.hasContractStarted" -> "Yes" for this to be valid
  it should " ask the correct next question when 'yesClientAgreed' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstituteYesClientAgreed
    val previousAnswers = List(personalService_workerSentActualSubstituteYesClientAgreed, "setup.hasContractStarted" -> "Yes")

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.workerPayActualSubstitute"

  }

  // CURRENT FLOW
  // added "setup.hasContractStarted" -> "Yes" for this to be valid
  it should " ask the correct next question when 'notAgreedWithClient' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstitute -> "personalService.workerSentActualSubstitute.notAgreedWithClient"
    val previousAnswers = List(currentQnA, "setup.hasContractStarted" -> "Yes")

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  // CURRENT FLOW
  // added "setup.hasContractStarted" -> "Yes" for this to be valid
  it should " ask the correct next question when 'noSubstitutionHappened' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstitute -> "personalService.workerSentActualSubstitute.noSubstitutionHappened"
    val previousAnswers = List(currentQnA, "setup.hasContractStarted" -> "Yes")

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteRejection"

  }

  // CURRENT FLOW
  // added "setup.hasContractStarted" -> "Yes" for this to be valid
  it should "Ask no more questions when" +
    "workerSentActualSubstitute -> 'yesClientAgreed'   " +
    " AND " +
    "workerPayActualSubstitute -> 'Yes' " in {
    val currentQnA = "personalService.workerPayActualSubstitute" -> "Yes"
    val previousAnswers = List(personalService_workerSentActualSubstituteYesClientAgreed, currentQnA, "setup.hasContractStarted" -> "Yes")

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true

  }

  // CURRENT FLOW
  // added "setup.hasContractStarted" -> "Yes" for this to be valid
  it should "Ask personalService.wouldWorkerPayHelper when" +
    " workerSentActualSubstitute -> 'yesClientAgreed' " +
    " AND " +
    "workerPayActualSubstitute -> 'No' " in {
    val currentQnA = "personalService.workerPayActualSubstitute" -> "No"
    val previousAnswers = List(personalService_workerSentActualSubstituteYesClientAgreed,currentQnA, "setup.hasContractStarted" -> "Yes")

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  // CURRENT FLOW
  // noSubstitutionHappened is the only time this question will be asked
  it should "Ask personalService.wouldWorkerPayHelper when " +
    "possibleSubstituteRejection -> 'wouldReject'" +
    " AND personalService.possibleSubstituteRejection is the current question" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject"
    val previousAnswers = List("setup.hasContractStarted" -> "Yes",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  it should "Ask possibleSubstituteWorkerPay when" +
    "setup.hasContractStarted -> No " +
    "AND " +
    "possibleSubstituteRejection -> wouldNotReject " in {

    val currentQnA = "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject"
    val previousAnswers = List("setup.hasContractStarted" -> "No",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteWorkerPay"
  }

  it should " ask the correct next question when 'Yes' is the answer to possibleSubstituteRejection" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject"
    val previousAnswers = List("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteWorkerPay"

  }

  it should " ask the correct next question when 'No' is the answer to possibleSubstituteWorkerPay and setup.hasContractStarted is Yes" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "No"
    val previousAnswers = List("setup.hasContractStarted" -> "Yes",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
      "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"
  }

  //CURRENT FLOW

  it should "possibleSubstituteWorkerPay -> 'Yes' " +
    " AND " +
    "setup.hasContractStarted is Yes" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "Yes"
    val previousAnswers = List("setup.hasContractStarted" -> "Yes",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
      "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  it should "possibleSubstituteWorkerPay -> 'No'" +
    "And setup.hasContractStarted is No" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "No"
    val previousAnswers = List("setup.hasContractStarted" -> "No",
      "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  it should "Ask no more questions when possibleSubstituteWorkerPay -> 'Yes'" +
    " AND " +
    "setup.hasContractStarted is No" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "Yes"
    val previousAnswers = List("setup.hasContractStarted" -> "No",
      "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldNotReject",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  //FUTURE FLOW
  it should " ask no more questions when possibleSubstituteRejection -> 'wouldReject' " +
    " AND " +
    "setup.hasContractStarted -> 'No'" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "personalService.possibleSubstituteRejection.wouldReject"
    val previousAnswers = List("setup.hasContractStarted" -> "No", currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  //FUTURE FLOW
  it should "Ask possibleSubstituteRejection when 'no' is the answer to setup.hasContractStarted" in {
    val maybeElement = personalServiceCluster.getStart(partialInterview_hasContractStarted_No)
    maybeElement.isDefined shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteRejection"

  }

}
