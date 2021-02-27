/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.httpParsers.DecisionHttpParser.DecisionReads
import javax.inject.Inject
import models._
import play.api.Logging
import play.api.libs.json.{Json, Writes}
import play.mvc.Http.Status._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpClient
import utils.DateTimeUtil

import scala.concurrent.{ExecutionContext, Future}

class DecisionConnector @Inject()(httpClient: HttpClient,
                                  servicesConfig: ServicesConfig,
                                  conf: FrontendAppConfig,
                                  dateTimeUtil: DateTimeUtil,
                                  timestamp: Timestamp) extends FeatureSwitching with Logging {

  lazy val baseUrl: String = servicesConfig.baseUrl("cest-decision")
  lazy val decideUrl = s"$baseUrl/cest-decision/decide"

  private[connectors] val handleUnexpectedError: PartialFunction[Throwable, Left[ErrorResponse, Nothing]] = {
    case ex: Exception => logger.error("[DecisionConnector][handleUnexpectedError]", ex)
      Left(ErrorResponse(INTERNAL_SERVER_ERROR, s"HTTP exception returned from decision API: ${ex.getMessage}"))
  }

  def decide(decisionRequest: Interview, writer: Writes[Interview] = Interview.writes)
               (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, DecisionResponse]] = {
    logger.debug(s"[DecisionConnector][decide] ${Json.toJson(decisionRequest)(writer)}")
    httpClient.POST(s"$decideUrl", decisionRequest)(writer, DecisionReads, hc, ec) recover handleUnexpectedError
  }
}
