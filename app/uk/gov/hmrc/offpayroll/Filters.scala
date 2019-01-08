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

import com.kenshoo.play.metrics.MetricsFilter
import uk.gov.hmrc.offpayroll.filters.SessionIdFilter
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.http.{DefaultHttpFilters, HttpFilters}
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter
import uk.gov.hmrc.play.bootstrap.filters.frontend.crypto.CookieCryptoFilter
import uk.gov.hmrc.play.bootstrap.filters.{CacheControlFilter, LoggingFilter, MicroserviceFilters}
import uk.gov.hmrc.play.bootstrap.filters.frontend.{FrontendAuditFilter, HeadersFilter, SessionTimeoutFilter}
import uk.gov.hmrc.play.bootstrap.filters.frontend.deviceid.DeviceIdFilter

@Singleton
class Filters @Inject()(
                                 configuration: Configuration,
                                 loggingFilter: LoggingFilter,
                                 headersFilter: HeadersFilter,
                                 securityFilter: SecurityHeadersFilter,
                                 frontendAuditFilter: FrontendAuditFilter,
                                 metricsFilter: MetricsFilter,
                                 deviceIdFilter: DeviceIdFilter,
                                 cookieCryptoFilter: CookieCryptoFilter,
                                 sessionTimeoutFilter: SessionTimeoutFilter,
                                 cacheControlFilter: CacheControlFilter,
                                 sessionFilter: SessionIdFilter
                               ) extends HttpFilters {

  val frontendFilters = Seq(
    metricsFilter,
    headersFilter,
    cookieCryptoFilter,
    deviceIdFilter,
    loggingFilter,
    frontendAuditFilter,
    sessionTimeoutFilter,
    cacheControlFilter,
    sessionFilter
  )

  lazy val enableSecurityHeaderFilter: Boolean =
    configuration.getBoolean("security.headers.filter.enabled").getOrElse(true)

  override val filters =
    if (enableSecurityHeaderFilter) Seq(securityFilter) ++ frontendFilters else frontendFilters

}
