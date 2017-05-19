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

class FinancialRiskClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {

  private val financialRiskCluster = FinancialRiskCluster

  private val topTag = "financialRisk.haveToPayButCannotClaim"

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("financialRisk")

  "The Financial Risk Cluster " should " have the correct name " in {
    financialRiskCluster.name shouldBe "financialRisk"
  }
  it should "have the correct clusterId " in {
    financialRiskCluster.clusterID shouldBe 4
  }
  it should "have the correct amount of question tags " in {
    financialRiskCluster.clusterElements.size shouldBe 3
  }

  it should "have the correct set of questions" in {
    assertAllElementsPresentForCluster(financialRiskCluster) shouldBe true
  }

  it should "ask no more questions If workerProvidedMaterials" in {
    val interviewMaterials = List(topTag -> "|financialRisk.workerProvidedMaterials")
    financialRiskCluster.shouldAskForDecision(interviewMaterials, interviewMaterials.head).isEmpty shouldBe true
  }

  it should "ask no more questions If workerProvidedEquipment" in {
    val interviewEquipment = List(topTag -> "|financialRisk.workerProvidedEquipment")
    financialRiskCluster.shouldAskForDecision(interviewEquipment, interviewEquipment.head).isEmpty shouldBe true
  }

  it should "ask no more questions If workerProvidedEquipment AND workerProvidedMaterials" in {
    val interviewMaterialAndEquipment = List(topTag -> "|financialRisk.workerProvidedEquipment|financialRisk.workerProvidedMaterials")
    financialRiskCluster.shouldAskForDecision(interviewMaterialAndEquipment, interviewMaterialAndEquipment.head).isEmpty shouldBe true
  }

  it should "ask no more questions If workerUsedVehicle AND workerHadOtherExpenses" in {
    val interviewVehicleOther = List(topTag -> "|financialRisk.workerUsedVehicle|financialRisk.workerHadOtherExpenses")
    financialRiskCluster.shouldAskForDecision(interviewVehicleOther, interviewVehicleOther.head).isEmpty shouldBe true
  }

  it should "ask no more questions If workerProvidedMaterial AND workerProvidedEquipment AND workerUsedVehicle AND workerHadOtherExpenses" in {
    val interviewMaterialEquipmentVehicleOther = List(
      topTag -> "|financialRisk.workerProvidedMaterial|financialRisk.workerProvidedEquipment|financialRisk.workerUsedVehicle|financialRisk.workerHadOtherExpenses")
    financialRiskCluster.shouldAskForDecision(interviewMaterialEquipmentVehicleOther, interviewMaterialEquipmentVehicleOther.head).isEmpty shouldBe true
  }

  it should "ask the correct next question If workerUsedVehicle " in {
    val interviewVehicle = List("financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerUsedVehicle")
    financialRiskCluster.shouldAskForDecision(interviewVehicle, interviewVehicle.head).get.questionTag shouldBe "financialRisk.workerMainIncome"
  }

  it should "ask the correct next question If workerHadOtherExpenses " in {
    val interviewOther = List("financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerHadOtherExpenses")
    financialRiskCluster.shouldAskForDecision(interviewOther, interviewOther.head).get.questionTag shouldBe "financialRisk.workerMainIncome"
  }

  it should "ask the correct next question If expensesAreNotRelevantForRole " in {
    val interviewExpensesAreNotRelevantForRole = List("financialRisk.haveToPayButCannotClaim" -> "|financialRisk.expensesAreNotRelevantForRole")
    financialRiskCluster.shouldAskForDecision(interviewExpensesAreNotRelevantForRole, interviewExpensesAreNotRelevantForRole.head).get.questionTag shouldBe "financialRisk.workerMainIncome"
  }


}
