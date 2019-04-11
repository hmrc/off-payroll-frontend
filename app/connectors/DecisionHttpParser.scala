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

import models.{DecisionResponse, ErrorResponse}
import play.api.Logger
import play.mvc.Http.Status.{OK, INTERNAL_SERVER_ERROR}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object DecisionHttpParser {

  implicit object DecisionReads extends HttpReads[Either[ErrorResponse, DecisionResponse]] {

    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, DecisionResponse] = {

      response.status match {
        case OK => {
          Logger.debug("Received an OK response from decide API")
          response.json.validate[DecisionResponse].fold(
            invalid => {
              Logger.warn(s"Received Invalid Json from decision connector - $invalid")
              Left(ErrorResponse(INTERNAL_SERVER_ERROR, "Invalid Json received from decision connector"))
            },
            decisionResponse => Right(decisionResponse)
          )
        }
        case status =>
          Logger.error(s"Unexpected response from decide API - Status $status Response: ${response.body}")
          Left(ErrorResponse(status, s"Unexpected Response. Response: ${response.body}"))
      }
    }
  }
}
