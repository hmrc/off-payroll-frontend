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

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.httpParsers.{DecisionHttpParser, LogHttpParser}
import models._
import models.logging.LogInterview
import play.api.libs.json.Json
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import play.mvc.Http.Status._
import utils.DateTimeUtil

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class DecisionConnector @Inject()(httpClient: HttpClient,
                                  servicesConfig: ServicesConfig,
                                  conf: FrontendAppConfig,
                                  dateTimeUtil: DateTimeUtil) {

  val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  val decideUrl = s"$baseUrl/cest-decision/decide"
  val logUrl = s"$baseUrl/cest-decision/log"

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    httpClient.POST(decideUrl, Json.toJson(decisionRequest)).map {
      DecisionHttpParser.reads
    }.recover {
      case ex: Exception => Left(ErrorResponse(INTERNAL_SERVER_ERROR,s"HTTP exception returned from decision API: ${ex.getMessage}"))
    }
  }

  def log(decisionRequest: Interview,decisionResponse: DecisionResponse)
         (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Boolean]] = {
    httpClient.POST(logUrl, Json.toJson(LogInterview(decisionRequest, decisionResponse, dateTimeUtil))) map LogHttpParser.reads
  }

}
