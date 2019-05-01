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
import connectors.DecisionHttpParser.DecisionReads
import javax.inject.{Inject, Singleton}

import models.WorkerType.SoleTrader
import models._
import play.api.Logger
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import play.mvc.Http.Status.INTERNAL_SERVER_ERROR

trait DecisionConnector {

  val baseUrl: String
  val decideUrl: String
  val logUrl: String

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]]
}

@Singleton
class DecisionConnectorImpl @Inject()(http: HttpClient,
                                      servicesConfig: ServicesConfig,
                                      conf: FrontendAppConfig) extends DecisionConnector {

  override val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  override val decideUrl = s"$baseUrl/cest-decision/decide"
  override val logUrl = s"$baseUrl/cest-decision/log"

  def decide(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {

    http.POST[Interview, Either[ErrorResponse, DecisionResponse]](decideUrl, decisionRequest).recover{
      case e: Exception =>
        Logger.error(s"Unexpected response from decide API - Response: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

  def log(decisionRequest: Interview)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {

    http.POST[LogInterview, Either[ErrorResponse, DecisionResponse]](logUrl, ).recover{
      case e: Exception =>
        Logger.error(s"Unexpected response from log API - Response: ${e.getMessage}")
        Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"Exception: ${e.getMessage}"))
    }
  }

  def x(decisionRequest: Interview, compressedInterview: String, decisionResult: DecisionResponse) = {
    LogInterview(
      conf.decisionVersion,
      compressedInterview,
      decisionRequest.provideServices match {
        case Some(workerType) if workerType == SoleTrader => "ESI"
        case None => "IR35"
      },
      decisionResult.result.toString,
      None,
      Setup(
        interview("setup")("endUserRole"),
        interview("setup")("hasContractStarted"),
        interview("setup")("provideServices")
      )
    )
  }

  def buildLogRequest(decisionRequest: Interview, compressedInterview: String, decision: DecisionResponse): LogInterview = {

    Logger.debug(s"---------------- Build LogInterview ------------------------------------")

    val interview: Map[String, Map[String, String]] = decisionRequest.

    val exit = Exit(
      interview("exit")("officeHolder")
    )

    val setup = Setup(
      interview("setup")("endUserRole"),
      interview("setup")("hasContractStarted"),
      interview("setup")("provideServices")
    )


    def getRoute(route: String): String = {
      if (route == "soleTrader") "ESI" else "IR35"
    }

    LogInterview(
      decisionRequest.version,
      compressedInterview,
      getRoute(setup.provideServices),
      decision.result,
      None,
      setup,
      exit,
      Option(PersonalService(interview)),
      Option(Control(interview)),
      Option(FinancialRisk(interview)),
      Option(PartAndParcel(interview)),
      new DateTime()
    )

  }
}