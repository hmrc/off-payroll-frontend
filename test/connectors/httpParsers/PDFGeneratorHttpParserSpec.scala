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