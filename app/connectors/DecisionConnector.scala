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

import javax.inject.{Inject, Singleton}

import config.FrontendAppConfig
import models._
import models.logging.LogInterview
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class DecisionConnector @Inject()(ws: WSClient,
                                  servicesConfig: ServicesConfig,
                                  conf: FrontendAppConfig) {

  val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  val decideUrl = s"$baseUrl/cest-decision/decide"
  val logUrl = s"$baseUrl/cest-decision/log"

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    ws.url(decideUrl).post(Json.toJson(decisionRequest)).map { response =>
      response.status match {
        case OK => Right(response.json.as[DecisionResponse])
        case unexpectedStatus@_ => Logger.error(s"Unexpected response from decide API - Response: $unexpectedStatus")
          Left(ErrorResponse(unexpectedStatus, s"Unexpected Response. Response: "))
      }
    }.recover {
      case e: Exception =>
        Logger.error(s"Exception from decide API: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

  def log(decisionRequest: Interview,
          decisionResult: DecisionResponse)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Boolean]] = {
    ws.url(logUrl).post(Json.toJson(LogInterview.createFromInterview(decisionRequest,decisionResult))).map { response =>
      response.status match {
        case NO_CONTENT => Right(true)
        case unexpectedStatus@_ => Logger.error(s"Unexpected response from log API - Response: $unexpectedStatus")
          Left(ErrorResponse(unexpectedStatus, s"Unexpected Response. Response: "))
      }
    }.recover {
      case e: Exception =>
        Logger.error(s"Exception from log API: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

}
