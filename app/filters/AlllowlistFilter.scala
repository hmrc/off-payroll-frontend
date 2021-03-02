/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package filters

import akka.stream.Materializer
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Call, RequestHeader, Result}
import uk.gov.hmrc.allowlist.AkamaiAllowlistFilter

import scala.concurrent.Future

@Singleton
class AlllowlistFilter @Inject()(val appConfig: FrontendAppConfig, implicit val mat: Materializer) extends AkamaiAllowlistFilter {

  override lazy val allowlist: Seq[String] = appConfig.allowlistedIps
  override lazy val destination: Call = Call("GET", appConfig.shutterPage)
  override lazy val excludedPaths: Seq[Call] = appConfig.allowlistExcludedPaths

  override def apply(requestFunc: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    if(appConfig.allowlistEnabled) super.apply(requestFunc)(requestHeader) else requestFunc(requestHeader)
  }
}
