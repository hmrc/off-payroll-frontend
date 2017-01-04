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

package uk.gov.hmrc.offpayroll.connectors

import org.scalatest.mockito.MockitoSugar
import org.mockito.Matchers._
import org.mockito.Mockito._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse}
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost}
import uk.gov.hmrc.play.http.ws.WSHttp
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import uk.gov.hmrc.offpayroll.modelsFormat._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by peter on 12/12/2016.
  */
class DecisionConnectorSpec extends UnitSpec with MockitoSugar with ServicesConfig with WithFakeApplication {

  implicit val hc = HeaderCarrier()

  private val version = "1.0.0-beta"
  private val correlationId = "12345"
  private val result = "Unknown"
  private val decisionResponseString =
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "12345",
      |  "score": {
      |    "personalService": "HIGH",
      |    "control": "LOW",
      |    "financialRiskA": "LOW",
      |    "financialRiskB": "LOW",
      |    "partAndParcel": "LOW",
      |    "businessStructure": "LOW"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin

  private val decisionRequestString =
    """
      |{
      |  "version": "1.0.0-beta",
      |  "correlationID": "12345",
      |  "interview": {
      |    "personalService": {
      |      "contractualObligationForSubstitute": "No"
      |    },
      |    "control": {
      |      "toldWhatToDo": "Sometimes"
      |    },
      |    "financialRiskA": {
      |      "workerPaidInclusive": "No"
      |    },
      |    "financialRiskB": {
      |      "workerProvideConsumablesMaterials": "No"
      |    },
      |    "partAndParcel": {
      |      "workerReceivesBenefits": "Yes"
      |    },
      |    "businessStructure": {
      |      "workerVAT": "No"
      |    }
      |  }
      |}
    """.stripMargin

  object testConnector extends DecisionConnector {
    override val decisionURL: String = "off-payroll-decision"
    override val serviceURL: String = "decide"
    override val http: HttpPost = mock[WSHttp]
  }


  "Calling /off-payroll-decision/decide" should {
    "return a decision" in {

      val decisionRequest = Json.fromJson[DecisionRequest](Json.parse(decisionRequestString)).get
      val jsonResponse = Json.fromJson[DecisionResponse](Json.parse(decisionResponseString)).get

      when(testConnector.http.POST[DecisionRequest, DecisionResponse](any(), any(), any())(any(), any(), any()))
        .thenReturn(Future(jsonResponse))

      val decideResponse = await(testConnector.decide(decisionRequest))

      decideResponse.version shouldBe version
      decideResponse.correlationID shouldBe correlationId
      decideResponse.score.size shouldBe 6

      for (score <- List("personalService", "control", "financialRiskA", "financialRiskB","partAndParcel", "businessStructure" )){
        decideResponse.score.contains(score) shouldBe true
      }

      decideResponse.result shouldBe result

    }

  }
}
