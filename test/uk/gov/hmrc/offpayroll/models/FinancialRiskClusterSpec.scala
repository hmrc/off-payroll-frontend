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

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("financialRisk")

  "The Financial Risk Cluster " should " have the correct name " in {
    financialRiskCluster.name shouldBe "financialRisk"
  }
  it should "have the correct clusterId " in {
    financialRiskCluster.clusterID shouldBe 2
  }
  it should "have the correct amount of question tags " in {
    financialRiskCluster.clusterElements.size shouldBe 3
  }

  it should "have the correct set of questions" in {
    assertAllElementsPresentForCluster(financialRiskCluster) shouldBe true
  }

  it should "decide on composed answer" in {

    val topTag = "financialRisk.haveToPayButCannotClaim"
    
    // If Materials OR Equipment is selected ask no more questions
    val interviewMaterials = List(topTag -> "|financialRisk.workerProvidedMaterials")
    val interviewEquipment = List(topTag -> "|financialRisk.workerProvidedEquipment")

    // If Materials AND Equipment is selected ask no more questions
    val interviewMaterialAndEquipment = interviewMaterials ++ interviewEquipment
    // TODO: this is passing because we have both separate OR definitions in flow, but AND is not working

    // If Vehicle AND Other Expenses are selected ask no more questions
    val interviewVehicleOther = List("financialRisk.workerUsedVehicle", "financialRisk.workerHadOtherExpenses").
      map(e => topTag -> s"|$e")

    // If Materials AND Equpment AND Vehicle AND other expenses are selected ask no more questions
    val interviewMaterialEquipmentVehicleOther = List(
      "financialRisk.workerProvidedMaterials",
      "financialRisk.workerProvidedEquipment",
      "financialRisk.workerUsedVehicle",
      "financialRisk.workerHadOtherExpenses").
      map(e => topTag -> s"|$e")


    // If Not relevent is selected continue with other questions
    // If Vehicle is selected continue with other questions
    val interviewVehicle = List("financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerUsedVehicle")

    // If Other Expenses is selected continue with other questions

    financialRiskCluster.shouldAskForDecision(interviewMaterials, interviewMaterials.head).isEmpty shouldBe true
    financialRiskCluster.shouldAskForDecision(interviewEquipment, interviewEquipment.head).isEmpty shouldBe true
    financialRiskCluster.shouldAskForDecision(interviewMaterialAndEquipment, interviewMaterialAndEquipment.head).isEmpty shouldBe true

    financialRiskCluster.shouldAskForDecision(interviewVehicleOther, interviewVehicleOther.head).isEmpty shouldBe true
    financialRiskCluster.shouldAskForDecision(interviewMaterialEquipmentVehicleOther, interviewMaterialEquipmentVehicleOther.head).isEmpty shouldBe true

    financialRiskCluster.shouldAskForDecision(interviewVehicle, interviewVehicle.head).get.questionTag shouldBe "financialRisk.workerMainIncome"

  }

}
