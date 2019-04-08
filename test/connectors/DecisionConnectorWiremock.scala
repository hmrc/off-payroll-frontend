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
      |    "financialRiskA": "LOW",
      |    "financialRiskB": "LOW",
      |    "partAndParcel": "LOW"
      |  },
      |  "result": "Unknown"
      |}
    """.stripMargin

  val body =
    s"""
       |{
       |"correlationID": "12345"
       |}
     """.stripMargin

  def mockForSuccessResponse(): StubMapping = {

    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(body))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(decisionResponseString)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockForSuccessResponse2(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(body))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(decisionResponseString)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockForFailureResponse(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(body))
      .willReturn(
        aResponse()
          .withStatus(400)))
  }

  def mockFor500FailureResponse(): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(body))
      .willReturn(
        aResponse()
          .withStatus(500)))
  }
}
