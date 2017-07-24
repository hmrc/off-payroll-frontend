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
import uk.gov.hmrc.offpayroll.resources._

import play.api.libs.json.Json._
import uk.gov.hmrc.offpayroll.modelsFormat._
import play.api.Logger


class LogInterviewBuilderSpec extends FlatSpec with Matchers  {


  private val decisionRequest = DecisionRequest(TEST_VERSION, TEST_CORRELATION_ID, completeInterview)
  private val decision = DecisionResponse(TEST_VERSION, TEST_CORRELATION_ID, Map("testKey" -> "testValue"), TEST_DECISION)

  val logInterview = LogInterviewBuilder.buildLogRequest(decisionRequest, TEST_COMPRESSED_INTERVIEW, decision)


  "A call to LogInterviewBuilder.buildLogRequest" should "return a populated LogInterview object" in {
    logInterview.isInstanceOf[LogInterview] shouldBe true
  }

  it should "contain the version passed in the decisionRequest in" in {
    logInterview.version shouldBe TEST_VERSION
  }

  it should "contain the compressed interview passed in" in {
    logInterview.compressedInterview shouldBe TEST_COMPRESSED_INTERVIEW
  }

  it should "contain the decision retrieved from the decisionResponse passed in" in {
    logInterview.decision shouldBe TEST_DECISION
  }

  it should "determine the route form the values passed in" in {
    logInterview.route shouldBe "IR35"
  }

  it should "create and populate an exit object from the interview passed in the decisionRequest" in {
    val exit = logInterview.exit
    exit.officeHolder shouldBe "Yes"
  }

  it should "create and populate a setup object from the interview passed in the decisionRequest" in {
    val setup = logInterview.setup
    setup.endUserRole shouldBe "endClient"
    setup.hasContractStarted shouldBe "Yes"
    setup.provideServices shouldBe "partnership"
  }

  it should "contain a valid PersonalService section" in {
    val personalService = logInterview.personalService
    personalService shouldBe defined
    personalService.get.workerSentActualSubstitute shouldBe defined
    personalService.get.workerSentActualSubstitute.get shouldBe "Yes"
    personalService.get.workerPayActualSubstitute shouldBe defined
    personalService.get.workerPayActualSubstitute.get shouldBe "No"
    personalService.get.possibleSubstituteRejection shouldBe defined
    personalService.get.possibleSubstituteRejection.get shouldBe "Yes"
    personalService.get.possibleSubstituteWorkerPay shouldBe defined
    personalService.get.possibleSubstituteWorkerPay.get shouldBe "No"
    personalService.get.wouldWorkerPayHelper shouldBe defined
    personalService.get.wouldWorkerPayHelper.get shouldBe "Yes"
  }

  it should "contain a valid Control section" in {
    val control = logInterview.control
    control shouldBe defined
    control.get.engagerMovingWorker shouldBe defined
    control.get.engagerMovingWorker.get shouldBe "Yes"
    control.get.workerDecidingHowWorkIsDone shouldBe defined
    control.get.workerDecidingHowWorkIsDone.get shouldBe "No"
    control.get.workHasToBeDone shouldBe defined
    control.get.workHasToBeDone.get shouldBe "Yes"
    control.get.workerDecideWhere shouldBe defined
    control.get.workerDecideWhere.get shouldBe "No"
  }

  it should "contain a valid FinancialRisk section" in {
    val financilRisk = logInterview.financialRisk
    financilRisk shouldBe defined
    financilRisk.get.workerProvidedMaterials shouldBe defined
    financilRisk.get.workerProvidedMaterials.get shouldBe "Yes"
    financilRisk.get.workerProvidedEquipment shouldBe defined
    financilRisk.get.workerProvidedEquipment.get shouldBe "No"
    financilRisk.get.workerUsedVehicle shouldBe defined
    financilRisk.get.workerUsedVehicle.get shouldBe "Yes"
    financilRisk.get.workerHadOtherExpenses shouldBe defined
    financilRisk.get.workerHadOtherExpenses.get shouldBe "No"
    financilRisk.get.expensesAreNotRelevantForRole shouldBe defined
    financilRisk.get.expensesAreNotRelevantForRole.get shouldBe "Yes"
    financilRisk.get.workerMainIncome shouldBe defined
    financilRisk.get.workerMainIncome.get shouldBe "No"
    financilRisk.get.paidForSubstandardWork shouldBe defined
    financilRisk.get.paidForSubstandardWork.get shouldBe "Yes"
  }

  it should "contain a valid PartAndParcel section" in {
    val partAndParcel = logInterview.partAndParcel
    partAndParcel shouldBe defined
    partAndParcel.get.workerReceivesBenefits shouldBe defined
    partAndParcel.get.workerReceivesBenefits.get shouldBe "Yes"
    partAndParcel.get.workerAsLineManager shouldBe defined
    partAndParcel.get.workerAsLineManager.get shouldBe "No"
    partAndParcel.get.contactWithEngagerCustomer shouldBe defined
    partAndParcel.get.contactWithEngagerCustomer.get shouldBe "Yes"
    partAndParcel.get.workerRepresentsEngagerBusiness shouldBe defined
    partAndParcel.get.workerRepresentsEngagerBusiness.get shouldBe "No"
  }


}
