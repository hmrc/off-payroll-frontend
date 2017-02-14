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

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader
import uk.gov.hmrc.offpayroll.resources._



/**
  * Created by peter on 11/12/2016.
  */
class PersonalServiceClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper{

  private val personalServiceCluster = PersonalServiceCluster

  "The Personal Service Cluster" should " have the correct name " in {
    personalServiceCluster.name shouldBe "personalService"
  }

  it should " have the correct clusterId " in {
    personalServiceCluster.clusterID shouldBe 0
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
      ("personalService.possibleSubstituteRejection", "Yes"),
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


  it should " ask the correct next question when 'yesClientAgreed' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstituteYesClientAgreed
    val previousAnswers = List(personalService_workerSentActualSubstituteYesClientAgreed)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.workerPayActualSubstitute"

  }


  it should " ask the correct next question when 'notAgreedWithClient' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstitute -> "personalService.workerSentActualSubstitute.notAgreedWithClient"
    val previousAnswers = List(currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  it should " ask the correct next question when 'noSubstitutionHappened' is the answer to workerSentActualSubstitute" in {
    val currentQnA = personalService_workerSentActualSubstitute -> "personalService.workerSentActualSubstitute.noSubstitutionHappened"
    val previousAnswers = List(currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteRejection"

  }

  it should " ask the correct next question when workerPayActualSubstitute is the current question but 'yesClientAgreed' is the answer to workerSentActualSubstitute" in {
    val currentQnA = "personalService.workerPayActualSubstitute" -> "Yes"
    val previousAnswers = List(personalService_workerSentActualSubstituteYesClientAgreed,currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  it should " ask the correct next question when 'No' is the answer to possibleSubstituteRejection" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "No"
    val previousAnswers = List("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  it should " ask the correct next question when 'No' is the answer to possibleSubstituteRejection - 2" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "No"
    val previousAnswers = List("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.yesClientAgreed",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"

  }

  it should " ask the correct next question when 'Yes' is the answer to possibleSubstituteRejection" in {
    val currentQnA = "personalService.possibleSubstituteRejection" -> "Yes"
    val previousAnswers = List("personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.possibleSubstituteWorkerPay"

  }

  it should " ask the correct next question when 'No' is the answer to possibleSubstituteWorkerPay and setup.hasContractStarted is Yes" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "No"
    val previousAnswers = List("setup.hasContractStarted" -> "Yes",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
      "personalService.possibleSubstituteRejection" -> "Yes",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "personalService.wouldWorkerPayHelper"
  }

  it should " ask no more questions when 'Yes' is the answer to possibleSubstituteWorkerPay and setup.hasContractStarted is Yes" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "Yes"
    val previousAnswers = List("setup.hasContractStarted" -> "Yes",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
      "personalService.possibleSubstituteRejection" -> "Yes",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  it should " ask no more questions when 'No' is the answer to possibleSubstituteWorkerPay and setup.hasContractStarted is No" in {
    val currentQnA = "personalService.possibleSubstituteWorkerPay" -> "No"
    val previousAnswers = List("setup.hasContractStarted" -> "No",
      "personalService.workerSentActualSubstitute" -> "personalService.workerSentActualSubstitute.noSubstitutionHappened",
      "personalService.possibleSubstituteRejection" -> "Yes",
      currentQnA)

    val maybeElement = personalServiceCluster.shouldAskForDecision(previousAnswers, currentQnA)

    maybeElement.isEmpty shouldBe true
  }

  it should " ask possibleSubstituteRejection when 'No' is the answer to setup.hasContractStarted" in {
    val maybeElement = personalServiceCluster.getStart(partialInterview_hasContractStarted_No)

    maybeElement.questionTag shouldBe "personalService.possibleSubstituteRejection"

  }

}
