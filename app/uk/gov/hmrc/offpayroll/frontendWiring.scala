/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll

import javax.inject.{Inject, Singleton}

import play.api.libs.ws.{WS, WSClient}
import uk.gov.hmrc.offpayroll.services.{FlowService, IR35FlowService}
import uk.gov.hmrc.play.audit.http.config.LoadAuditingConfig
import uk.gov.hmrc.play.audit.http.connector.{AuditConnector => Auditing}
import uk.gov.hmrc.play.config.{AppName, RunMode, ServicesConfig}
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost, WSPut}
import play.api.libs.ws.WSClient
import uk.gov.hmrc.offpayroll.connectors.{DecisionConnector, PdfGeneratorConnector}
import uk.gov.hmrc.offpayroll.models.LogInterview
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost}

import scala.concurrent.Future


trait ServiceRegistry extends ServicesConfig {
  lazy val decisionConnector: DecisionConnector = new FrontendDecisionConnector
  lazy val flowservice: FlowService = IR35FlowService()
}

object FrontendAuditConnector extends Auditing with AppName {
  override lazy val auditingConfig = LoadAuditingConfig(s"auditing")
}

object WSHttp extends WSGet with WSPut with WSPost with WSDelete with AppName with RunMode {
  override val hooks = NoneRequired
}

object FrontendAuthConnector extends AuthConnector with ServicesConfig {
  val serviceUrl = baseUrl("auth")
  lazy val http = WSHttp
}

@Singleton
class FrontendDecisionConnector extends DecisionConnector with ServicesConfig {
  override val decisionURL: String = baseUrl("off-payroll-decision")
  override val serviceLogURL: String = "off-payroll-decision/log"
  override val serviceDecideURL = "off-payroll-decision/decide"
  override val http = WSHttp

}

@Singleton
class FrontendPdfGeneratorConnector @Inject() (ws: WSClient) extends PdfGeneratorConnector with ServicesConfig {
  val pdfServiceUrl: String = baseUrl("pdf-generator-service")
  val serviceURL = pdfServiceUrl + "/pdf-generator-service/generate"
  val http = WSHttp
  def getWsClient:WSClient = ws
}
