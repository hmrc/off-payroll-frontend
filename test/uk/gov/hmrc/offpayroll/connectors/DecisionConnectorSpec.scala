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

package uk.gov.hmrc.offpayroll.connectors

import org.scalatest.mock.MockitoSugar
import org.mockito.Matchers._
import org.mockito.Mockito._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.offpayroll.models.{DecideRequest, DecideResponse}
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

  val decisionResponseString = "{\n  \"version\": \"0.0.1-alpha\",\n  \"correlationID\": " +
    "\"12345\",\n  \"carryOnWithQuestions\": true,\n  \"score\": {\n    \"personalService\": \"HIGH\",\n    \"miscellaneous\": \"LOW\"\n  },\n  \"result\": \"Unknown\"\n}"

  val decisionRequestString = "{\"version\":\"0.0.1-alpha\",\"correlationID\":\"123456\",\"interview\":" +
    "{\"personalService\":{\"personalService.workerSentActualSubstitiute\":\"false\"}}}"

  object testConnector extends DecisionConnector {
    override val decisionURL: String = "off-payroll-decision"
    override val serviceURL: String = "decide"
    override val http: HttpPost = mock[WSHttp]
  }


  "Calling /off-payroll-decision/decide" should {
    "return a decision" in {

      val decisionRequest = Json.fromJson[DecideRequest](Json.parse(decisionRequestString)).get
      val jsonResponse = Json.fromJson[DecideResponse](Json.parse(decisionResponseString)).get

      when(testConnector.http.POST[DecideRequest, DecideResponse](any(), any(), any())(any(), any(), any()))
        .thenReturn(Future(jsonResponse))

      val result = await(testConnector.decide(decisionRequest))

      result.version shouldBe ("0.0.1-alpha")
      result.correlationID shouldBe ("12345")
      result.score.size shouldBe (2)
      result.result shouldBe ("Unknown")


    }
//    "return an illegible response" in {
//      val jsonResponse = Json.fromJson[EligibilityStatus](checkEligibilityFalseResponse).get
//
//      when(testConnector.http.POST[SelfAssessment, EligibilityStatus](any(), any(), any())(any(), any(), any()))
//        .thenReturn(Future(jsonResponse))
//
//      val result = await(testConnector.checkEligibility(checkEligibilityFalseRequest))
//
//      result.eligible shouldBe false
//      result.reasons.contains("TotalDebtIsTooHigh") shouldBe true
//    }
  }
}
