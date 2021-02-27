/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services

import akka.util.ByteString
import base.GuiceAppSpecBase
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF}
import connectors.mocks.MockPDFGeneratorConnector
import play.twirl.api.Html

import scala.concurrent.Future


class PDFServiceSpec extends GuiceAppSpecBase with MockPDFGeneratorConnector {

  val testHtml = Html("<html><title>Test</title></html>")

  object TestPDFService extends PDFService(mockPdfGeneratorConnector)

  "PDFService" should {

    s"when calling .generatePdf()" when {

      "A valid response is returned" should {

        "return a SuccessfulPDF model" in {
          val expected = Right(SuccessfulPDF(ByteString("")))
          mockGeneratePdf(testHtml)(Future.successful(expected))
          val actual = await(TestPDFService.generatePdf(testHtml))

          actual mustBe expected
        }
      }

      "An error response is returned" should {

        "return an error model" in {
          val expected = Left(BadRequest)
          mockGeneratePdf(testHtml)(Future.successful(expected))
          val actual = await(TestPDFService.generatePdf(testHtml))

          actual mustBe expected
        }
      }
    }
  }
}