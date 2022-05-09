/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package config

import java.util.Base64

import com.google.inject.{Inject, Singleton}
import config.featureSwitch.{DecisionServiceVersionFeature, FeatureSwitching}
import controllers.routes
import play.api.Environment
import play.api.i18n.Lang
import play.api.mvc.{Call, Request}
import uk.gov.hmrc.play.bootstrap.binders.SafeRedirectUrl
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject() (environment: Environment, val servicesConfig: ServicesConfig) {

  private lazy val contactHost = servicesConfig.getString("contact-frontend.host")
  private val contactFormServiceIdentifier = "off-payroll"

  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"

  private lazy val exitSurveyBaseUrl = servicesConfig.getString("feedback-frontend.host") + servicesConfig.getString("feedback-frontend.url")
  lazy val exitSurveyUrl = s"$exitSurveyBaseUrl/$contactFormServiceIdentifier"

  private def allowlistConfig(key: String): Seq[String] =
    Some(new String(Base64.getDecoder.decode(servicesConfig.getString(key)), "UTF-8"))
      .map(_.split(",")).getOrElse(Array.empty).toSeq

  lazy val allowlistEnabled: Boolean = servicesConfig.getBoolean("allowlist.enabled")
  lazy val allowlistedIps: Seq[String] = allowlistConfig("allowlist.allowedIps")
  lazy val allowlistExcludedPaths: Seq[Call] = allowlistConfig("allowlist.excludedPaths").map(path => Call("GET", path))
  lazy val shutterPage: String = servicesConfig.getString("allowlist.shutter-page-url")

  private def requestPath(implicit request: Request[_]) = SafeRedirectUrl(host + request.path).encodedUrl
  def feedbackUrl(implicit request: Request[_]): String =
    s"$contactHost/contact/beta-feedback-unauthenticated?service=$contactFormServiceIdentifier&backUrl=$requestPath"
  def reportAccessibilityIssueUrl(problemPageUri: String)(implicit request: Request[_]): String =
    s"$contactHost/contact/accessibility-unauthenticated?service=" +
      s"$contactFormServiceIdentifier&userAction=${SafeRedirectUrl(host + problemPageUri).encodedUrl}"

  lazy val loginUrl: String = servicesConfig.getString("urls.login")
  lazy val loginContinueUrl: String = servicesConfig.getString("urls.loginContinue")

  lazy val host: String = servicesConfig.getString("host")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)

  lazy val mongoTtl: Int = servicesConfig.getInt("mongodb.timeToLiveInSeconds")

  def decisionVersion: String = FeatureSwitching.getValue(DecisionServiceVersionFeature)(this)

  lazy val pdfGeneratorService: String = servicesConfig.baseUrl("pdf-generator-service")
  lazy val assetsFrontendUrl: String = servicesConfig.getString("assets.url")
  lazy val assetsFrontendVersion: String = servicesConfig.getString("assets.version")

  lazy val timeoutPeriod: Int = servicesConfig.getInt("timeout.period")
  lazy val timeoutCountdown: Int = servicesConfig.getInt("timeout.countdown")

  lazy val govUkStartPageUrl: String = servicesConfig.getString("urls.govUkStartPage")
  lazy val employmentStatusManualUrl: String = servicesConfig.getString("urls.employmentStatusManual")
  lazy val employmentStatusManualChapter5Url: String = servicesConfig.getString("urls.employmentStatusManualChapter5")
  lazy val employmentStatusManualESM0527Url: String = servicesConfig.getString("urls.employmentStatusManualESM0527")
  lazy val employmentStatusManualESM0521Url: String = servicesConfig.getString("urls.employmentStatusManualESM0521")
  lazy val employmentStatusManualESM11160Url: String = servicesConfig.getString("urls.employmentStatusManualESM11160")
  lazy val officeHolderUrl: String = servicesConfig.getString("urls.officeHolder")
  lazy val understandingOffPayrollUrl: String = servicesConfig.getString("urls.understandingOffPayroll")
  lazy val feePayerResponsibilitiesUrl: String = servicesConfig.getString("urls.feePayerResponsibilities")
  lazy val payeForEmployersUrl: String = servicesConfig.getString("urls.payeForEmployers")
  lazy val govukAccessibilityStatementUrl: String = servicesConfig.getString("urls.govukAccessibilityStatement")
  lazy val abilityNetUrl: String = servicesConfig.getString("urls.abilityNet")
  lazy val wcagUrl: String = servicesConfig.getString("urls.wcag")
  lazy val eassUrl: String = servicesConfig.getString("urls.eass")
  lazy val ecniUrl: String = servicesConfig.getString("urls.ecni")
  lazy val hmrcAdditionalNeedsUrl: String = servicesConfig.getString("urls.hmrcAdditionalNeeds")

}
