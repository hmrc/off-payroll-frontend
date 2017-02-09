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

    val currentQnA = personalService_workerSentActualSubstitute -> "Yes"
    val fullanswers = PropertyFileLoader.transformMapToAListOfAnswers(PropertyFileLoader.getMessagesForACluster("personalService"))
    val decision = personalServiceCluster.shouldAskForDecision(fullanswers, currentQnA)

    decision.nonEmpty shouldBe false
  }

}
