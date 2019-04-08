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
import play.api.Logger
import play.api.http.Status.OK
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, Upstream4xxResponse, Upstream5xxResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DecisionConnector extends ServicesConfig {

  val baseUrl: String
  val decideUrl: String

  def decide(decideRequest: String)(implicit hc: HeaderCarrier): Future[String]
}

@Singleton
class FrontendDecisionConnector @Inject()(http: HttpClient) extends DecisionConnector {

  override val baseUrl: String = baseUrl("off-payroll-decision")
  override val decideUrl = s"$baseUrl/off-payroll-decision/decide"

  def decide(decideRequest: String)(implicit hc: HeaderCarrier): Future[String] = {

    http.POST[String, HttpResponse](decideUrl, "").map { response =>
      response.status match {
        case OK =>
          Logger.info("Received an OK response from decide API")
          Json.parse(response.body).as[String]

        case status  => throw new Exception(s"Unexpected response from decide API - Status $status")
      }
    } recover {
      case e: Upstream4xxResponse => throw new Exception(s"Unexpected 4XX response from decide API - ${e.getMessage}")
      case e: Upstream5xxResponse => throw new Exception(s"Unexpected 5XX response from decide API - ${e.getMessage}")
      case e: Exception => throw new Exception(s"Unexpected exception from decide API - ${e.getMessage}")
    }
  }

}