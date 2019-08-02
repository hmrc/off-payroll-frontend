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
import connectors.httpParsers.DecisionHttpParser.DecisionReads
import connectors.httpParsers.LogHttpParser.LogReads
import javax.inject.Inject
import models._
import models.logging.LogInterview
import play.api.Logger
import play.api.libs.json.{JsBoolean, JsObject, JsValue, Json, Writes}
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.DateTimeUtil
import viewmodels.Timestamp

import scala.concurrent.{ExecutionContext, Future}

class DecisionConnector @Inject()(httpClient: HttpClient,
                                  servicesConfig: ServicesConfig,
                                  conf: FrontendAppConfig,
                                  dateTimeUtil: DateTimeUtil,
                                  dataCacheConnector: DataCacheConnector,
                                  timestamp: Timestamp
                                 ) {

  lazy val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  lazy val decideUrl = s"$baseUrl/cest-decision/decide"
  lazy val logUrl = s"$baseUrl/cest-decision/log"

  private[connectors] val handleUnexpectedError: PartialFunction[Throwable, Left[ErrorResponse, Nothing]] = {
    case ex: Exception => Logger.error("[DecisionConnector][handleUnexpectedError]",ex)
      Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API: ${ex.getMessage}"))
  }

  def toMap(jsonOld: JsValue, jsonNew: JsValue, identical: Boolean = false): Map[String, JsValue] = {

    Map(
      "oldResponse" -> jsonOld,
      "newResponse" -> jsonNew,
      "identicalResult" -> JsBoolean(identical)
    )
  }

  def calculateDifferences(oldResponse: Future[Either[ErrorResponse, DecisionResponse]], newResponse: Future[Either[ErrorResponse,DecisionResponse]])
                          (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[CacheMap] = {

    oldResponse.flatMap{
      oldResponse =>

        newResponse.flatMap{
          newResponse =>

            val cacheMap: Map[String, JsValue] = (oldResponse, newResponse) match {
              case (Right(responseOld), Right(responseNew)) =>

                val identical: Boolean = responseOld.result.equals(responseNew.result)
                toMap(Json.toJson(responseOld), Json.toJson(responseNew), identical)

              case (Left(errorOld), Right(responseNew)) => toMap(Json.toJson(errorOld), Json.toJson(responseNew))

              case (Right(responseOld), Left(errorNew)) => toMap(Json.toJson(responseOld),Json.toJson(errorNew))

              case (Left(errorOld), Left(errorNew)) =>

                val identical: Boolean = errorOld.equals(errorNew)
                toMap(Json.toJson(errorOld),Json.toJson(errorNew), identical)
            }

            dataCacheConnector.save(CacheMap(timestamp.timestamp(), cacheMap))
        }
    }
  }

  def decide(decisionRequest: Interview, writer: Writes[Interview] = Interview.writes)
            (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    Logger.debug(s"[DecisionConnector][decide] ${Json.toJson(decisionRequest)(writer)}")

    val response = httpClient.POST(decideUrl, decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError

    val newResponse = decideNew(decisionRequest)

    calculateDifferences(response,newResponse)

    response
  }

  def decideNew(decisionRequest: Interview, writer: Writes[Interview] = NewInterview.writes)
            (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    Logger.debug(s"[DecisionConnector][decideNew] ${Json.toJson(decisionRequest)(writer)}")

    httpClient.POST(s"$decideUrl/new", decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError
  }

  def log(decisionRequest: Interview,decisionResponse: DecisionResponse)
         (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Boolean]] = {
    Logger.debug(s"[DecisionConnector][log] ${Json.toJson(decisionRequest)}")
    httpClient.POST(logUrl, LogInterview(decisionRequest, decisionResponse, dateTimeUtil))(LogInterview.writes, LogReads, hc, ec) recover handleUnexpectedError
  }
}
