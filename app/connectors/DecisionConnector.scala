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

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.httpParsers.DecisionHttpParser.DecisionReads
import connectors.httpParsers.LogHttpParser.LogReads
import javax.inject.Inject
import models._
import models.logging.LogInterview
import play.api.Logger
import play.api.libs.json.{Json, Writes}
import play.mvc.Http.Status._
import repositories.ParallelRunningRepository
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.DateTimeUtil

import scala.concurrent.{ExecutionContext, Future}

class DecisionConnector @Inject()(httpClient: HttpClient,
                                  servicesConfig: ServicesConfig,
                                  conf: FrontendAppConfig,
                                  dateTimeUtil: DateTimeUtil,
                                  parallelRunningRepository: ParallelRunningRepository,
                                  timestamp: Timestamp
                                 ) extends FeatureSwitching {

  lazy val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  lazy val decideUrl = s"$baseUrl/cest-decision/decide"
  lazy val logUrl = s"$baseUrl/cest-decision/log"

  private[connectors] val handleUnexpectedError: PartialFunction[Throwable, Left[ErrorResponse, Nothing]] = {
    case ex: Exception => Logger.error("[DecisionConnector][handleUnexpectedError]", ex)
      Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API: ${ex.getMessage}"))
  }

  def decide(decisionRequest: Interview, writer: Writes[Interview] = Interview.writes)
               (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    Logger.debug(s"[DecisionConnector][decide] ${Json.toJson(decisionRequest)(writer)}")

    httpClient.POST(s"$decideUrl", decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError
  }

  def log(decisionRequest: Interview, decisionResponse: DecisionResponse)
         (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Boolean]] = {
    Logger.debug(s"[DecisionConnector][log] ${Json.toJson(decisionRequest)}")
    httpClient.POST(logUrl, LogInterview(decisionRequest, decisionResponse, dateTimeUtil))(LogInterview.writes, LogReads, hc, ec) recover handleUnexpectedError
  }
}
