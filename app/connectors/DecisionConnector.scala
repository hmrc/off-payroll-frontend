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
import javax.inject.{Inject, Singleton}
import models.Interview
import play.api.{Configuration, Environment, Logger, Mode}
import play.api.http.Status.OK
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, Upstream4xxResponse, Upstream5xxResponse}
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DecisionConnector {

  val baseUrl: String
  val decideUrl: String

  def decide(decideRequest: Interview)(implicit hc: HeaderCarrier): Future[String]
}

@Singleton
class FrontendDecisionConnector @Inject()(http: HttpClient,
                                          servicesConfig: ServicesConfig,
                                          conf: FrontendAppConfig) extends DecisionConnector {

  override val baseUrl: String = servicesConfig.baseUrl("off-payroll-decision")
  override val decideUrl = s"$baseUrl/off-payroll-decision/decide"

  case class Score(personalService: String, control: String, financialRiskA: String, financialRiskB: String, partAndParcel: String)
  case class model(version: String, correlationID: String, score: Score, result: String)

  def decide(decideRequest: Interview)(implicit hc: HeaderCarrier): Future[model] = {

    http.POST[Interview, HttpResponse](decideUrl, decideRequest).map { response =>
      response.status match {
        case OK =>
          Logger.info("Received an OK response from decide API")
          Json.parse(response.body).as[model]

        case status  => throw new Exception(s"Unexpected response from decide API - Status $status")
      }
    } recover {
      case e: Upstream4xxResponse => throw new Exception(s"Unexpected 4XX response from decide API - ${e.getMessage}")
      case e: Upstream5xxResponse => throw new Exception(s"Unexpected 5XX response from decide API - ${e.getMessage}")
      case e: Exception => throw new Exception(s"Unexpected exception from decide API - ${e.getMessage}")
    }
  }

}