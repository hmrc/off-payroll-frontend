/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.service

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json
import uk.gov.hmrc.offpayroll.models.{DecisionBuilder, DecisionRequest, OffPayrollWebflow}


import uk.gov.hmrc.offpayroll.modelsFormat._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by peter on 13/12/2016.
  */
class DecisionBuilderSpec  extends FlatSpec with Matchers {

  val interview = Map("personalService.workerSentActualSubstitiute" -> "false", "personalService.contractrualRight" -> "true",
    "control.hasMoreThan50Percent" -> "false")


  val decisionRequestStringPlusControl =
    """
      |{
      |  "version": "1.0.1-beta",
      |  "correlationID": "12345",
      |  "interview": {
      |    "personalService": {
      |      "workerSentActualSubstitiute": "false",
      |      "contractrualRight": "true"
      |    },
      |    "control": {
      |      "hasMoreThan50Percent": "false"
      |    }
      |  }
      |}
    """.stripMargin

  val decisionRequestString =
    """
      |{
      |  "version": "1.0.1-beta",
      |  "correlationID": "12345",
      |  "interview": {
      |    "personalService": {
      |      "workerSentActualSubstitiute": "false"
      |    }
      |  }
      |}
    """.stripMargin

  val expectedDecisionRequest = Json.fromJson[DecisionRequest](Json.parse(decisionRequestStringPlusControl)).get

  "A DecisionBuilder " should "build a DecisionRequest from the current Interview in the Sesssion" in {
    val decisionRequest: DecisionRequest = DecisionBuilder.buildDecisionRequest(interview)

    decisionRequest shouldBe (expectedDecisionRequest)

  }



}
