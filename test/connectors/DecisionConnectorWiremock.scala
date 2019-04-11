/*
 * Copyright 2019 HM Revenue & Customs
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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping

class DecisionConnectorWiremock {

  private val decisionResponseString =
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "12345",
      |  "score": {
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRisk": "LOW",
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin

  private val fullDecisionResponseString =
    """
      |{
      |  "version": "1.5.0-final",
      |  "correlationID": "12345",
      |  "score": {
      |    "setup": "CONTINUE",
      |    "exit": "CONTINUE",
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRisk": "LOW",
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Self-Employed"
      |}
    """.stripMargin

  val emptyInterview =
    s"""
       |{
       |  "version" : "1.5.0-final",
       |  "correlationID" : "12345",
       |  "interview" : {
       |    "setup" : { },
       |    "exit" : { },
       |    "personalService" : { },
       |    "control" : { },
       |    "financialRisk" : { },
       |    "partAndParcel" : { }
       |  }
       |}
     """.stripMargin

  val fullInterview =
    s"""
       |{
       |  "version": "1.5.0-final",
       |  "correlationID": "12345",
       |  "interview": {
       |    "setup": {
       |      "endUserRole": "personDoingWork",
       |      "hasContractStarted": "No",
       |      "provideServices": "soleTrader"
       |    },
       |    "exit": {
       |      "officeHolder": "No"
       |    },
       |    "personalService": {
       |      "workerSentActualSubstitute": "yesClientAgreed",
       |      "workerPayActualSubstitute": "No",
       |      "possibleSubstituteRejection": "wouldReject",
       |      "possibleSubstituteWorkerPay": "Yes",
       |      "wouldWorkerPayHelper": "Yes"
       |    },
       |    "control": {
       |      "engagerMovingWorker": "canMoveWorkerWithPermission",
       |      "workerDecidingHowWorkIsDone": "workerAgreeWithOthers",
       |      "whenWorkHasToBeDone": "workerAgreeSchedule",
       |      "workerDecideWhere": "workerChooses"
       |    },
       |    "financialRisk": {
       |      "workerProvidedMaterials": "No",
       |      "workerProvidedEquipment": "No",
       |      "workerUsedVehicle": "No",
       |      "workerHadOtherExpenses": "No",
       |      "expensesAreNotRelevantForRole": "No",
       |      "workerMainIncome": "incomeCalendarPeriods",
       |      "paidForSubstandardWork": "asPartOfUsualRateInWorkingHours"
       |    },
       |    "partAndParcel": {
       |      "workerReceivesBenefits": "Yes",
       |      "workerAsLineManager": "Yes",
       |      "contactWithEngagerCustomer": "No",
       |      "workerRepresentsEngagerBusiness": "workAsIndependent"
       |    }
       |  }
       |}
     """.stripMargin

  def mockForSuccessResponse(): StubMapping = {

    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(emptyInterview))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(decisionResponseString)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockForSuccessFullInterviewResponse(): StubMapping = {

    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(fullInterview))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(fullDecisionResponseString)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockForSuccessResponse2(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(emptyInterview))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(decisionResponseString)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockFor499FailureResponse(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(emptyInterview))
      .willReturn(
        aResponse()
          .withStatus(499)))
  }

  def mockFor400FailureResponse(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(emptyInterview))
      .willReturn(
        aResponse()
          .withStatus(400)))
  }

  def mockFor500FailureResponse(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(emptyInterview))
      .willReturn(
        aResponse()
          .withStatus(500)))
  }
}
