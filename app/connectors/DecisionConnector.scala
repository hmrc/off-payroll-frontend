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

import java.util.UUID

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.httpParsers.DecisionHttpParser.DecisionReads
import connectors.httpParsers.LogHttpParser.LogReads
import javax.inject.Inject
import models._
import models.logging.LogInterview
import play.api.Logger
import play.api.libs.json.{JsValue, Json, Writes}
import play.mvc.Http.Status._
import repositories.ParallelRunningRepository
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.DateTimeUtil

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

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

    Logger.debug(s"[DecisionConnector][decide] url: $decideUrl")
    Logger.debug(s"[DecisionConnector][decide] body: ${Json.toJson(decisionRequest)(writer)}")

    val response = httpClient.POST(decideUrl, decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError

    implicit val appConf: FrontendAppConfig = conf
    implicit val interview = decisionRequest

    if (!isEnabled(OptimisedFlow)) {
      for {
        oldResponse <- response
        newResponse <- decideNew(decisionRequest)
      } yield calculateDifferences(oldResponse, newResponse)
    }

    response
  }

  def calculateDifferences(oldResponse: Either[ErrorResponse, DecisionResponse],
                           newResponse: Either[ErrorResponse, DecisionResponse])
                          (implicit hc: HeaderCarrier, ec: ExecutionContext, decisionRequest: Interview): Future[ParallelRunningModel] = {

    val model: ParallelRunningModel = (oldResponse, newResponse) match {
      case (Right(responseOld), Right(responseNew)) =>

        val identicalBody: Boolean = responseOld.equals(responseNew)
        val identicalResult: Boolean = responseOld.result.equals(responseNew.result)
        toModel(Json.toJson(responseOld), Json.toJson(responseNew), identicalBody, identicalResult)

      case (Left(errorOld), Left(errorNew)) =>

        val identicalBody: Boolean = errorOld.equals(errorNew)
        val identicalResult: Boolean = errorOld.status.equals(errorNew.status)
        toModel(Json.toJson(errorOld), Json.toJson(errorNew), identicalBody, identicalResult)

      case (Right(responseOld), Left(responseNew)) => toModel(Json.toJson(responseOld), Json.toJson(responseNew))
      case (Left(responseOld), Right(responseNew)) => toModel(Json.toJson(responseOld), Json.toJson(responseNew))
    }

    parallelRunningRepository.insert(model).map { _ => model }
  }

  def toModel(oldResponse: JsValue,
              newResponse: JsValue,
              identicalBody: Boolean = false,
              identicalResult: Boolean = false)(implicit decisionRequest: Interview): ParallelRunningModel = {

    val oldRequest = Json.toJson(decisionRequest)(Interview.writes)
    val newRequest = Json.toJson(decisionRequest)(NewInterview.writes)

    if (!identicalResult) {
      Logger.error("[DecisionConnector] The new decision result did not match the old decision result:\n\n" +
        s"OldRequest: $oldRequest\n\n" +
        s"NewRequest: $newRequest\n\n" +
        s"OldResponse: $oldResponse\n\n" +
        s"NewResponse: $newResponse")
    }
    if (!identicalBody && identicalResult) Logger.warn("[DecisionConnector] The decision results match but the json bodies differ." +
      s" OldResponse: $oldResponse , NewResponse: $newResponse")

    def uuid: String = UUID.randomUUID().toString

    ParallelRunningModel(
      _id = s"${timestamp.timestamp()} - $uuid",
      oldRequest = oldRequest,
      newRequest = newRequest,
      oldResponse = oldResponse,
      newResponse = newResponse,
      identicalBody = identicalBody,
      identicalResult = identicalResult
    )
  }

  def decideNew(decisionRequest: Interview, writer: Writes[Interview] = NewInterview.writes)
               (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    Logger.debug(s"[DecisionConnector][decideNew] ${Json.toJson(decisionRequest)(writer)}")

    httpClient.POST(s"$decideUrl", decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError
  }

  def log(decisionRequest: Interview, decisionResponse: DecisionResponse)
         (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Boolean]] = {
    Logger.debug(s"[DecisionConnector][log] ${Json.toJson(decisionRequest)}")
    httpClient.POST(logUrl, LogInterview(decisionRequest, decisionResponse, dateTimeUtil))(LogInterview.writes, LogReads, hc, ec) recover handleUnexpectedError
  }
}
