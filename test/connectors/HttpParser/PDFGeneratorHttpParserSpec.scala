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
import connectors.HttpParsers.PDFGeneratorHttpParser
import connectors.HttpParsers.PDFGeneratorHttpParser.{BadRequest, SuccessfulPDF, UnexpectedError}
import play.api.http.Status
import uk.gov.hmrc.http.HttpResponse

class PDFGeneratorHttpParserSpec extends SpecBase {

  "The PDFGeneratorHttpParser.reads" when {

    "the http response status is NO_CONTENT" should {

      "return the expected model" in {
        PDFGeneratorHttpParser.reads.read("", "", HttpResponse(Status.OK, responseString = Some("PDF"))) mustBe
          Right(SuccessfulPDF("PDF"))
      }
    }

    "the http response status is BAD_REQUEST" should {

      "return the expected model" in {
        PDFGeneratorHttpParser.reads.read("", "", HttpResponse(Status.BAD_REQUEST, responseString = Some("Err"))) mustBe
          Left(BadRequest)
      }
    }

    "the http response status is unexpected" should {

      "return an ErrorModel" in {
        PDFGeneratorHttpParser.reads.read("", "", HttpResponse(Status.FAILED_DEPENDENCY)) mustBe
          Left(UnexpectedError(Status.FAILED_DEPENDENCY))
      }
    }
  }
}