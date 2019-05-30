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

import models.ErrorResponse
import play.api.Logger
import play.mvc.Http.Status.NO_CONTENT
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object LogHttpParser {

  implicit object LogReads extends HttpReads[Either[ErrorResponse, Boolean]] {

    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, Boolean] = {
      response.status match {
        case NO_CONTENT => Right(true)
        case unexpectedStatus@_ =>
          Logger.error(s"[LogHttpParser][LogReads] Unexpected response from log API - Response: $unexpectedStatus: ${response.body}")
          Left(ErrorResponse(response.status,"Unexpected Response returned from log API"))
      }
    }
  }
}
