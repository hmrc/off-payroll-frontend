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

package uk.gov.hmrc.offpayroll

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.models.{BusinessStructureCluster}

/**
  * Created by peter on 11/12/2016.
  */
class BusinessStructureClusterSpec extends FlatSpec with Matchers {

  private val businessStructureCluster = BusinessStructureCluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("businessStructure")

  "The Business Structure Cluster "
  it should " ask for a decision when similarWork is 0-3 " in {
    val currentQnA = ("businessStructure.similarWork", "0-3")
    val partialAnswers = List(("businessStructure.similarWork", "0-3"))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }
  it should " ask for a decision when similarWork is 10+ " in {
    val currentQnA = ("businessStructure.similarWork", "10+")
    val partialAnswers = List(("businessStructure.similarWork", "10+"))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).isEmpty shouldBe true
  }
  it should " not ask for a decision when similarWork is 4-9 " in {
    val currentQnA = ("businessStructure.similarWork", "4-9")
    val partialAnswers = List(("businessStructure.similarWork", "4-9"))

    businessStructureCluster.shouldAskForDecision(partialAnswers, currentQnA).nonEmpty shouldBe true
  }
  it should " ask the correct next question for a given question" in {
    val currentQnA = ("businessStructure.businessAccount", "Y")
    val partialAnswers = List(("businessStructure.similarWork", "4-9"),("businessStructure.workerVAT", "Y"),
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
}
