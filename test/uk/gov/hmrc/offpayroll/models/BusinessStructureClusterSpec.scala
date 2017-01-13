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

class BusinessStructureClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper{

  private val businessStructureCluster = BusinessStructureCluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("businessStructure")
  val businessTsructure_zeroToThree: String = "businessStructure.similarWork.zeroToThree"
  val businessTsructure_fourToNine: String = "businessStructure.similarWork.fourToNine"
  val businessTsructure_tenPlus: String = "businessStructure.similarWork.tenPlus"

  "The Business Structure Cluster " should " have the correct name " in {
    businessStructureCluster.name shouldBe "businessStructure"
  }
  it should " have the correct clusterId " in {
    businessStructureCluster.clusterID shouldBe 5
  }
  it should " have the correct amount of question tags " in {
    businessStructureCluster.clusterElements.size shouldBe 8
  }
  it should " ask for a decision when similarWork is zero to three " in {

    val currentQnA = ("businessStructure.similarWork", businessTsructure_zeroToThree)
    val partialAnswers = List(("businessStructure.similarWork", businessTsructure_zeroToThree))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }
  it should " ask for a decision when similarWork is ten plus " in {
    val currentQnA = ("businessStructure.similarWork", businessTsructure_tenPlus)
    val partialAnswers = List(("businessStructure.similarWork", businessTsructure_tenPlus))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }
  it should " not ask for a decision when similarWork is four to nine " in {
    val currentQnA = ("businessStructure.similarWork", businessTsructure_fourToNine)
    val partialAnswers = List(("businessStructure.similarWork", businessTsructure_fourToNine))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe false
  }
  it should " ask the correct next question for a given question" in {
    val currentQnA = ("businessStructure.businessAccount", "Y")
    val partialAnswers = List(("businessStructure.similarWork", businessTsructure_fourToNine),("businessStructure.workerVAT", "Y"),
      ("businessStructure.businessAccount", "Y"))

    val maybeElement = businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "businessStructure.advertiseForWork"
  }
  it should " ask for a decision when all questions have been asked" in {
    val currentQnA = ("businessStructure.workerPaysForInsurance", "Y")
    val allAnswers = PropertyFileLoader.transformMapToAListOfAnswers(propsFilteredByCluster)

    businessStructureCluster.shouldAskForDecision(allAnswers, currentQnA).isEmpty shouldBe true
  }
  it should " have the correct set of questions" in {
    assertAllElementsPresentForCluster(businessStructureCluster) shouldBe true
  }
}
