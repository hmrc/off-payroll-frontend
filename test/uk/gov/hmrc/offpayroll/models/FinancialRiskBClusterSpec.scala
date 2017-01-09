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

class FinancialRiskBClusterSpec extends FlatSpec with Matchers {

  private val financialRiskBCluster = FinancialRiskBCluster

  private val propsFilteredByCluster = PropertyFileLoader.getMessagesForACluster("financialRiskB")

  "The Financial Risk B Cluster "
  it should " have the correct name " in {
    financialRiskBCluster.name shouldBe "financialRiskB"
  }
  it should " have the correct clusterId " in {
    financialRiskBCluster.clusterID shouldBe 3
  }
  it should " have the correct amount of question tags " in {
    financialRiskBCluster.clusterElements.size shouldBe 11
  }
  it should " ask the correct next question when No is the answer to financialRiskB.provideVehicle" in {
    val currentQnA = ("financialRiskB.provideVehicle", "No")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.workerIncurExpense"
  }
  it should " ask the correct next question when Yes is the answer to financialRiskB.provideVehicle" in {
    val currentQnA = ("financialRiskB.provideVehicle", "Yes")
    val partialAnswers = List(("financialRiskB.provideVehicle", "Yes"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.engagerPayVehicleExpense"
  }
  it should " ask the correct next question when No is the answer to financialRiskB.engagerPayOtherExpense" in {
    val currentQnA = ("financialRiskB.workerIncurExpense", "No")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),("financialRiskB.workerIncurExpense", "No"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.workerMainIncome"
  }
  it should " ask the correct next question when 'incomeCommission' is the answer to financialRiskB.workerMainIncome" in {
    val currentQnA = ("financialRiskB.workerMainIncome", "incomeCommission")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeCommission"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.workerProvideAtTheirExpense"
  }
  it should " ask the correct next question when 'incomeRateByInvoice' is the answer to financialRiskB.workerMainIncome" in {
    val currentQnA = ("financialRiskB.workerMainIncome", "incomeRateByInvoice")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeRateByInvoice"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.workerSufferedLatePayment"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskB.workerProvideAtTheirExpense" in {
    val currentQnA = ("financialRiskB.workerProvideAtTheirExpense", "No")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeCommission"),
      ("financialRiskB.workerProvideAtTheirExpense", "No"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.paidForSubstandardWork"
  }
  it should " ask the correct next question when 'Yes' is the answer to financialRiskB.workerFixAtTheirOwnTime" in {
    val currentQnA = ("financialRiskB.workerFixAtTheirOwnTime", "Yes")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeCommission"),
      ("financialRiskB.workerProvideAtTheirExpense", "Yes"),
      ("financialRiskB.workerFixAtTheirOwnTime", "Yes"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.paidForSubstandardWork"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskB.workerFixAtTheirOwnTime" in {
    val currentQnA = ("financialRiskB.workerFixAtTheirOwnTime", "No")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeCommission"),
      ("financialRiskB.workerProvideAtTheirExpense", "Yes"),
      ("financialRiskB.workerFixAtTheirOwnTime", "No"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.nonEmpty shouldBe true
    maybeElement.get.questionTag shouldBe "financialRiskB.workerPayForMaterialsSubstandardWorker"
  }
  it should " ask the correct next question when 'No' is the answer to financialRiskB.paidForSubstandardWork" in {
    val currentQnA = ("financialRiskB.paidForSubstandardWork", "No")
    val partialAnswers = List(("financialRiskB.provideVehicle", "No"),
      ("financialRiskB.workerIncurExpense", "No"),
      ("financialRiskB.workerMainIncome", "incomeCommission"),
      ("financialRiskB.workerProvideAtTheirExpense", "No"),
      ("financialRiskB.paidForSubstandardWork", "No"))

    val maybeElement = financialRiskBCluster.shouldAskForDecision(partialAnswers, currentQnA)
    maybeElement.isEmpty shouldBe true
  }
  it should " ask for a decision when all questions have been asked " in {
    val currentQnA = ("financialRiskB.actualEngagerWithholdPayment", "Yes")
    val allAnswers = PropertyFileLoader.transformMapToAListOfAnswers(propsFilteredByCluster)

    financialRiskBCluster.shouldAskForDecision(allAnswers, currentQnA).isEmpty shouldBe true
  }
}
