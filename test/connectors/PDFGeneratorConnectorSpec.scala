/*
 * Copyright 2022 HM Revenue & Customs
 *
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