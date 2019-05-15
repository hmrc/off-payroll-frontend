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

import models.{DecisionResponse, ErrorResponse}
import play.api.Logger
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HttpResponse

object DecisionHttpParser {

  val reads: HttpResponse => Either[ErrorResponse, DecisionResponse] = response =>
    response.status match {
      case OK => response.json.validate[DecisionResponse].asOpt.fold {
        Logger.error(s"Invalid Json received from decision connector from decision API")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR,"Invalid Json received from decision API")): Either[ErrorResponse,DecisionResponse]}{ validJson =>
        Right(validJson)
      }
      case unexpectedStatus@_ => Logger.error(s"Unexpected response from decision API - Response: $unexpectedStatus")
        Left(ErrorResponse(response.status,"Unexpected Response returned from decision API"))
    }

}