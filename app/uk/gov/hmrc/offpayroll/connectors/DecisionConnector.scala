/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.connectors

import javax.inject.{Inject, Singleton}
import play.api.{Configuration, Environment, Logger}
import play.api.http.Status.{INTERNAL_SERVER_ERROR, NOT_FOUND}
import uk.gov.hmrc.http.{HeaderCarrier, HttpPost, HttpResponse, NotFoundException}
import uk.gov.hmrc.offpayroll.WSHttp
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse, LogInterview}
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.play.config.ServicesConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DecisionConnector {

  val decisionURL: String
  val serviceDecideURL: String
  val serviceLogURL: String
  val http: HttpPost

  def decide(decideRequest: DecisionRequest)(implicit hc: HeaderCarrier): Future[DecisionResponse] = {
    http.POST[DecisionRequest, DecisionResponse](s"$decisionURL/$serviceDecideURL/", decideRequest)
  }

  def log(logInterview: LogInterview)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
    http.POST[LogInterview, HttpResponse](s"$decisionURL/$serviceLogURL/", logInterview).map {
      result =>
        result
    } recover {
      case e : NotFoundException =>
        Logger.error(s"Not Found Exception while calling Offpayroll Decision to log analytics ${e.getMessage}")
        HttpResponse.apply(NOT_FOUND)
      case e : Exception =>
        Logger.error(s"Exception while connecting to Offpayroll Decision to log analytics ${e.getMessage}")
        HttpResponse.apply(INTERNAL_SERVER_ERROR)
    }
  }
}

@Singleton
class FrontendDecisionConnector @Inject()(
                                           override val runModeConfiguration: Configuration,
                                           environment: Environment
                                         ) extends DecisionConnector with ServicesConfig {
  override protected def mode = environment.mode
  override val decisionURL: String = baseUrl("off-payroll-decision")
  override val serviceLogURL: String = "off-payroll-decision/log"
  override val serviceDecideURL = "off-payroll-decision/decide"
  override val http = WSHttp
}
