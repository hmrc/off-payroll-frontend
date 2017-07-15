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

import com.kenshoo.play.metrics.PlayModule
import org.joda.time.DateTime
import org.scalatest.mockito.MockitoSugar
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse}
import uk.gov.hmrc.play.config.ServicesConfig
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost}
import uk.gov.hmrc.play.http.ws.WSHttp
//import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.play.test._
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.offpayroll.models._
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by brianheathcote on 12/07/2017.
  */
class LogInterviewConnectorSpec extends UnitSpec with MockitoSugar with ServicesConfig with WithFakeApplication with ScalaFutures {
  implicit val hc = HeaderCarrier()

  override def bindModules = Seq(new PlayModule)

  private val exit = Exit("Yes")
  private val setup = Setup("personDoingWork","Yes","partnership")
  private val logInterview = LogInterview("0.1", "jghdfsu@#", "IR35", "OUT", Option("123"), setup, exit, Option.empty,
    Option.empty, Option.empty, Option.empty, new DateTime())


  private val version = ""
  private val correlationId = ""
  private val result = "test"
  private val decisionResponseString =
    """
      |{
      |  "version": "",
      |  "correlationID": "",
      |  "score": {
      |    "personalService": "",
      |    "control": "",
      |    "financialRiskA": "",
      |    "financialRiskB": "",
      |    "partAndParcel": ""
      |  },
      |  "result": "test"
      |}
    """.stripMargin


  object logInterviewConnector extends LogInterviewConnector {
    override val decisionURL: String = "off-payroll-decision"
    override val serviceURL: String = "log"
    override val http: HttpPost = mock[WSHttp]
  }


  "Calling /off-payroll-decision/log" should {
    "return a decision" in {

      val jsonResponse = Json.fromJson[DecisionResponse](Json.parse(decisionResponseString)).get

      when(logInterviewConnector.http.POST[LogInterview, DecisionResponse](any(), any(), any())(any(), any(), any()))
        .thenReturn(Future(jsonResponse))

      val logResponse = await(logInterviewConnector.log(logInterview))

      logResponse.version shouldBe version
      logResponse.result shouldBe result

    }

  }

}
