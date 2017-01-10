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

class FinancialRiskAClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {

  private val financialRiskACluster = FinancialRiskACluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("financialRiskA")

  "The Financial Risk A Cluster "
  it should " have the correct name " in {
    financialRiskACluster.name shouldBe "financialRiskA"
  }
  it should " have the correct clusterId " in {
    financialRiskACluster.clusterID shouldBe 2
  }
  it should " have the correct amount of question tags " in {
    financialRiskACluster.clusterElements.size shouldBe 7
  }
  it should " not ask any more questions when 'Yes' is the answer to financialRiskA.workerPaidInclusive" in {
    val currentQnA = ("financialRiskA.workerPaidInclusive", "Yes")
    val previousAnswers = List(("financialRiskA.workerPaidInclusive", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.workerPaidInclusive" in {
    val currentQnA = ("financialRiskA.workerPaidInclusive", "No")
    val previousAnswers = List(("financialRiskA.workerPaidInclusive", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerNeedConsumablesMaterials"
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskA.workerNeedConsumablesMaterials" in {
    val currentQnA = ("financialRiskA.workerNeedConsumablesMaterials", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerProvideConsumablesMaterials"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.workerNeedConsumablesMaterials" in {
    val currentQnA = ("financialRiskA.workerNeedConsumablesMaterials", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerNeedEquipment"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.workerProvideConsumablesMaterials" in {
    val currentQnA = ("financialRiskA.workerProvideConsumablesMaterials", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerNeedEquipment"
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskA.workerProvideConsumablesMaterials" in {
    val currentQnA = ("financialRiskA.workerProvideConsumablesMaterials", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.engagerPayConsumables"
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskA.engagerPayConsumables" in {
    val currentQnA = ("financialRiskA.engagerPayConsumables", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerNeedEquipment"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.engagerPayConsumables" in {
    val currentQnA = ("financialRiskA.engagerPayConsumables", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskA.workerNeedEquipment" in {
    val currentQnA = ("financialRiskA.workerNeedEquipment", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.workerProvideEquipment"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.workerNeedEquipment" in {
    val currentQnA = ("financialRiskA.workerNeedEquipment", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskA.workerProvideEquipment" in {
    val currentQnA = ("financialRiskA.workerProvideEquipment", "Yes")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "Yes"),
      ("financialRiskA.workerProvideEquipment", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskA.engagerPayEquipment"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskA.workerProvideEquipment" in {
    val currentQnA = ("financialRiskA.workerProvideEquipment", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "Yes"),
      ("financialRiskA.workerProvideEquipment", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " not ask anymore questions when 'No' is the answer to financialRiskA.engagerPayEquipment" in {
    val currentQnA = ("financialRiskA.engagerPayEquipment", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "Yes"),
      ("financialRiskA.workerProvideEquipment", "Yes"),
      ("financialRiskA.engagerPayEquipment", "No"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " not ask anymore questions when 'Yes' is the answer to financialRiskA.engagerPayEquipment" in {
    val currentQnA = ("financialRiskA.engagerPayEquipment", "No")
    val previousAnswers = List(
      ("financialRiskA.workerPaidInclusive", "No"),
      ("financialRiskA.workerNeedConsumablesMaterials", "Yes"),
      ("financialRiskA.workerProvideConsumablesMaterials", "Yes"),
      ("financialRiskA.engagerPayConsumables", "Yes"),
      ("financialRiskA.workerNeedEquipment", "Yes"),
      ("financialRiskA.workerProvideEquipment", "Yes"),
      ("financialRiskA.engagerPayEquipment", "Yes"))

    val maybeElement = financialRiskACluster.shouldAskForDecision(previousAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }

  it should " have the correct set of questions" in {
    assertAllElementsPresentForCluster(financialRiskACluster) shouldBe true
  }
}
