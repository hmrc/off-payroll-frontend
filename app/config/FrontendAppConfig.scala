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

package config

import java.util.Base64

import com.google.inject.{Inject, Singleton}
import config.featureSwitch.{DecisionServiceVersionFeature, FeatureSwitching}
import controllers.routes
import play.api.Environment
import play.api.i18n.Lang
import play.api.mvc.{Call, Request}
import uk.gov.hmrc.play.binders.ContinueUrl
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}

@Singleton
class FrontendAppConfig @Inject() (environment: Environment, val servicesConfig: ServicesConfig, runMode: RunMode) {

  private lazy val contactHost = servicesConfig.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "off-payroll"

  lazy val analyticsToken = servicesConfig.getString(s"google-analytics.token")
  lazy val analyticsHost = servicesConfig.getString(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private lazy val exitSurveyBaseUrl = servicesConfig.getString("feedback-frontend.host") + servicesConfig.getString("feedback-frontend.url")
  lazy val exitSurveyUrl = s"$exitSurveyBaseUrl/$contactFormServiceIdentifier"

  private def whitelistConfig(key: String): Seq[String] =
    Some(new String(Base64.getDecoder.decode(servicesConfig.getString(key)), "UTF-8"))
      .map(_.split(",")).getOrElse(Array.empty).toSeq

  lazy val whitelistEnabled: Boolean = servicesConfig.getBoolean("whitelist.enabled")
  lazy val whitelistedIps: Seq[String] = whitelistConfig("whitelist.allowedIps")
  lazy val whitelistExcludedPaths: Seq[Call] = whitelistConfig("whitelist.excludedPaths").map(path => Call("GET", path))
  lazy val shutterPage: String = servicesConfig.getString("whitelist.shutter-page-url")

  private def requestUri(implicit request: Request[_]) = ContinueUrl(host + request.uri).encodedUrl
  def feedbackUrl(implicit request: Request[_]): String =
    s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier&backUrl=$requestUri"

  lazy val loginUrl = servicesConfig.getString("urls.login")
  lazy val loginContinueUrl = servicesConfig.getString("urls.loginContinue")

  lazy val host: String = servicesConfig.getString("host")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val mongoTtl: Int = servicesConfig.getInt("mongodb.timeToLiveInSeconds")

  def decisionVersion = FeatureSwitching.getValue(DecisionServiceVersionFeature)(this)

  lazy val pdfGeneratorService = servicesConfig.baseUrl("pdf-generator-service")

  lazy val timeoutPeriod: Int = servicesConfig.getInt("timeout.period")
  lazy val timeoutCountdown: Int = servicesConfig.getInt("timeout.countdown")

  lazy val govUkStartPageUrl = servicesConfig.getString("urls.govUkStartPage")
  lazy val employmentStatusManualUrl = servicesConfig.getString("urls.employmentStatusManual")
  lazy val employmentStatusManualChapter5Url = servicesConfig.getString("urls.employmentStatusManualChapter5")
  lazy val employmentStatusManualESM0527Url = servicesConfig.getString("urls.employmentStatusManualESM0527")
  lazy val officeHolderUrl = servicesConfig.getString("urls.officeHolder")
  lazy val understandingOffPayrollUrl = servicesConfig.getString("urls.understandingOffPayroll")
  lazy val feePayerResponsibilitiesUrl = servicesConfig.getString("urls.feePayerResponsibilities")
  lazy val payeForEmployersUrl = servicesConfig.getString("urls.payeForEmployers")

}
