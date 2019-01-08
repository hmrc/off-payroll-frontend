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

package uk.gov.hmrc.offpayroll

import akka.actor.ActorSystem
import com.typesafe.config.Config
import play.api.{Configuration, Play}
import play.api.Mode.Mode
import play.api.Play.current
import uk.gov.hmrc.http._
import uk.gov.hmrc.http.hooks.HttpHooks
import uk.gov.hmrc.play.audit.http.HttpAuditing
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.config.LoadAuditingConfig
import uk.gov.hmrc.play.config.AppName
import uk.gov.hmrc.play.http.ws.{WSDelete, WSGet, WSPost, WSPut}

object FrontendAuditConnector extends AppName with AuditConnector with OffPayrollConfig {
  override lazy val auditingConfig = LoadAuditingConfig(appNameConfiguration, mode, "auditing")
}

trait OffPayrollConfig {
  protected def appNameConfiguration: Configuration = Play.current.configuration
  protected def runModeConfiguration: Configuration = Play.current.configuration
  protected def mode: Mode = Play.current.mode
  protected def actorSystem: ActorSystem = Play.current.actorSystem
}

trait Hooks extends HttpHooks with HttpAuditing {
  override val hooks = Seq(AuditingHook)
}

trait WSHttp extends HttpGet with WSGet with HttpPut with WSPut with HttpPost with WSPost with HttpDelete with WSDelete with Hooks with AppName
object WSHttp extends WSHttp with OffPayrollConfig {
  override protected def configuration: Option[Config] = Some(current.configuration.underlying)

  override def auditConnector: AuditConnector = FrontendAuditConnector
}
