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

  def mockForSuccessResponse(request: String, response: String): StubMapping = {

    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(request))
      .willReturn(
        aResponse()
          .withStatus(200)
          .withBody(response)
          .withHeader("Content-Type", "application/json; charset=utf-8")))
  }

  def mockForFailureResponse(request: String, responseStatus: Int): StubMapping = {
    stubFor(post(urlEqualTo("/off-payroll-decision/decide"))
      .withRequestBody(equalToJson(request))
      .willReturn(
        aResponse()
          .withStatus(responseStatus)))
  }
}
