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

package services

import akka.util.ByteString
import base.SpecBase
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF}
import connectors.mocks.MockPDFGeneratorConnector
import play.twirl.api.Html

import scala.concurrent.Future


class PDFServiceSpec extends SpecBase with MockPDFGeneratorConnector {

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