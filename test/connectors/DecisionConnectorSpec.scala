/*
* Copyright 2018 HM Revenue & Customs
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

import config.FrontendAppConfig
import models.Interview
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.test.UnitSpec


class DecisionConnectorSpec extends MockitoSugar with UnitSpec with ConnectorTests {

  implicit val headerCarrier = new HeaderCarrier()

  val mockHttp = mock[HttpClient]
  val servicesConfig = mock[ServicesConfig]
  implicit val configuration = mock[FrontendAppConfig]

  val connector: DecisionConnector = new FrontendDecisionConnector(mockHttp, servicesConfig, configuration)

  val decisionConnectorWiremock = new DecisionConnectorWiremock

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
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin

  val model: Interview = Interview(
    "12345"
  )

  "Calling the decide connector" should {
    "return a decision based on the interview" in {
      decisionConnectorWiremock.mockForSuccessResponse()

      val clientResponse = await(connector.decide(model))
      clientResponse shouldBe ""
    }

    "return an error if a bad request is returned" in {
      decisionConnectorWiremock.mockForFailureResponse()

      val clientResponse = intercept[Exception](await(connector.decide(model)))
      clientResponse.getMessage shouldBe ""
    }
    "return an error a 500 is returned" in {
      decisionConnectorWiremock.mockFor500FailureResponse()

      val clientResponse = intercept[Exception](await(connector.decide(model)))
      clientResponse.getMessage shouldBe "Unexpected 5XX response"
    }
  }
}
