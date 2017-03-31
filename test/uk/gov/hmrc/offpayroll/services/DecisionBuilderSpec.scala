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

package uk.gov.hmrc.offpayroll.services

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json
import uk.gov.hmrc.offpayroll.models.{DecisionBuilder, DecisionRequest, OffPayrollWebflow}
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.offpayroll.util.TestConfigurationHelper.getString
import uk.gov.hmrc.play.test.WithFakeApplication

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by peter on 13/12/2016.
  */
class DecisionBuilderSpec  extends FlatSpec with Matchers with WithFakeApplication {
  private val TEST_CORRELATION_ID = "00000001099"
  val interview1 = Map("personalService.workerSentActualSubstitiute" -> "false", "personalService.contractrualRight" -> "true",
    "control.hasMoreThan50Percent" -> "false", "control.toldWhatToDo" -> "control.toldWhatToDo.sometimes",
    "financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerProvidedMaterials|financialRisk.workerProvidedEquipment",
    "financialRisk.workerMainIncome" -> "financialRisk.workerMainIncome.incomeCalendarPeriods"
  )
  val interview2 = Map("personalService.workerSentActualSubstitiute" -> "false", "personalService.contractrualRight" -> "true",
    "control.hasMoreThan50Percent" -> "false", "control.toldWhatToDo" -> "control.toldWhatToDo.sometimes",
    "financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerProvidedMaterials")

  private val offPayrollDecisionVersion = getString("microservice.services.off-payroll-decision.version")

  private val decisionRequestStringPlusControl1 =
    s"""
      |{
      |  "version": "${offPayrollDecisionVersion}",
      |  "correlationID": "00000001099",
      |  "interview": {
      |    "personalService": {
      |      "workerSentActualSubstitiute": "false",
      |      "contractrualRight": "true"
      |    },
      |    "control": {
      |      "hasMoreThan50Percent": "false",
      |      "toldWhatToDo": "sometimes"
      |    },
      |    "financialRisk": {
      |    "workerProvidedMaterials": "Yes",
      |    "workerProvidedEquipment": "Yes",
      |    "workerMainIncome": "incomeCalendarPeriods"
      |    }
      |  }
      |}
    """.stripMargin
  private val decisionRequestStringPlusControl2 =
    s"""
      |{
      |  "version": "${offPayrollDecisionVersion}",
      |  "correlationID": "00000001099",
      |  "interview": {
      |    "personalService": {
      |      "workerSentActualSubstitiute": "false",
      |      "contractrualRight": "true"
      |    },
      |    "control": {
      |      "hasMoreThan50Percent": "false",
      |      "toldWhatToDo": "sometimes"
      |    },
      |    "financialRisk": {
      |    "workerProvidedMaterials": "Yes"
      |    }
      |  }
      |}
    """.stripMargin

  val expectedDecisionRequest1: DecisionRequest = Json.fromJson[DecisionRequest](Json.parse(decisionRequestStringPlusControl1)).get
  val expectedDecisionRequest2: DecisionRequest = Json.fromJson[DecisionRequest](Json.parse(decisionRequestStringPlusControl2)).get

  "A DecisionBuilder " should "build a DecisionRequest from the current Interview in the Session with multiple group value" in {
    val decisionRequest: DecisionRequest = DecisionBuilder.buildDecisionRequest(interview1, TEST_CORRELATION_ID)
    decisionRequest shouldBe expectedDecisionRequest1
    decisionRequest.correlationID shouldBe TEST_CORRELATION_ID
  }

  "A DecisionBuilder " should "build a DecisionRequest from the current Interview in the Session with single group value" in {
    val decisionRequest: DecisionRequest = DecisionBuilder.buildDecisionRequest(interview2, TEST_CORRELATION_ID)
    decisionRequest shouldBe expectedDecisionRequest2
    decisionRequest.correlationID shouldBe TEST_CORRELATION_ID
  }
}
