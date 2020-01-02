/*
 * Copyright 2020 HM Revenue & Customs
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

import base.GuiceAppSpecBase
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF}
import connectors.mocks.MockWsClient
import models.PdfRequest
import play.api.http.Status
import play.api.libs.json.Json
import play.twirl.api.Html

import scala.concurrent.Future


class PDFGeneratorConnectorSpec extends GuiceAppSpecBase with MockWsClient {

  val testHtml = Html("<html><title>Test</title></html>")

  object TestPDFGeneratorConnector extends PDFGeneratorConnector(mockWsClient, frontendAppConfig)

  "PDFGeneratorConnector" should {

    "have the correct url to the PDF Generator Service" in {
      TestPDFGeneratorConnector.url mustBe s"${frontendAppConfig.pdfGeneratorService}/pdf-generator-service/generate"
    }

    s"when calling .generatePdf()" when {

      "A valid response is returned" should {

        "return a SuccessfulPDF model" in {
          val response = wsResponse(Status.OK, "PDF")
          setupMockHttpPost(TestPDFGeneratorConnector.url, Json.toJson(PdfRequest(testHtml)))(Future.successful(response))

          val expected = Right(SuccessfulPDF(response.bodyAsBytes))
          val actual = await(TestPDFGeneratorConnector.generatePdf(testHtml))

          actual mustBe expected
        }
      }

      "An error response is returned" should {

        "return an error model" in {
          val response = wsResponse(Status.BAD_REQUEST, "")
          setupMockHttpPost(TestPDFGeneratorConnector.url, Json.toJson(PdfRequest(testHtml)))(Future.successful(response))

          val expected = Left(BadRequest)
          val actual = await(TestPDFGeneratorConnector.generatePdf(testHtml))

          actual mustBe expected
        }
      }
    }
  }
}