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

package uk.gov.hmrc.offpayroll

import javax.inject.{Inject, Singleton}
import play.api.Mode.Mode
import play.api.{Configuration, Environment, Play}
import play.api.libs.ws.WSClient
import uk.gov.hmrc.http._
import uk.gov.hmrc.http.hooks.HttpHooks
import uk.gov.hmrc.offpayroll.connectors.PdfGeneratorConnector
import uk.gov.hmrc.play.audit.http.HttpAuditing
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.config.{AppName, ServicesConfig}
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost, WSPut}

trait offPayrollConfig {
  protected def appNameConfiguration:Configuration = Play.current.configuration
  protected def mode:Mode = Play.current.mode
  protected def runModeConfiguration: Configuration = Play.current.configuration
  def auditConnector : AuditConnector = auditConnector
}

trait Hooks extends HttpHooks with HttpAuditing {
  override val hooks = Seq(AuditingHook)
}

trait WSHttp extends HttpGet with WSGet with HttpPut with WSPut with HttpPost with WSPost with HttpDelete with WSDelete with Hooks with AppName
object WSHttp extends WSHttp with offPayrollConfig


@Singleton
class FrontendPdfGeneratorConnector @Inject()(
                                              override val runModeConfiguration: Configuration,
                                              environment: Environment,
                                              ws: WSClient
                                            ) extends PdfGeneratorConnector with ServicesConfig {
  override protected def mode = environment.mode
  val pdfServiceUrl: String = baseUrl("pdf-generator-service")
  val serviceURL = pdfServiceUrl + "/pdf-generator-service/generate"
  val http = WSHttp
  def getWsClient:WSClient = ws
}
