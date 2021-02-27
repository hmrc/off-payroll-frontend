/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors.httpParsers

import base.SpecBase
import connectors.httpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF, UnexpectedError}
import connectors.utils.WsUtils
import play.api.http.Status


class PDFGeneratorHttpParserSpec extends SpecBase with WsUtils {

  "The PDFGeneratorHttpParser.reads" when {

    "the http response status is OK" should {

      "return the expected model" in {
        val testResponse = wsResponse(Status.OK, "PDF")
        PDFGeneratorHttpParser.reads(testResponse) mustBe Right(SuccessfulPDF(testResponse.bodyAsBytes))
      }
    }

    "the http response status is BAD_REQUEST" should {

      "return the expected model" in {
        val testResponse = wsResponse(Status.BAD_REQUEST, "")
        PDFGeneratorHttpParser.reads(testResponse) mustBe Left(BadRequest)
      }
    }

    "the http response status is unexpected" should {

      "return an ErrorModel" in {
        val testResponse = wsResponse(Status.FAILED_DEPENDENCY, "")
        PDFGeneratorHttpParser.reads(testResponse) mustBe Left(UnexpectedError(Status.FAILED_DEPENDENCY))
      }
    }
  }
}