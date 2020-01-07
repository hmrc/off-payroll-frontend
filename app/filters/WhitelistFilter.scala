/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package filters

import akka.stream.Materializer
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Call, RequestHeader, Result}
import uk.gov.hmrc.whitelist.AkamaiWhitelistFilter

import scala.concurrent.Future

@Singleton
class WhitelistFilter @Inject()(val appConfig: FrontendAppConfig, implicit val mat: Materializer) extends AkamaiWhitelistFilter {

  override lazy val whitelist: Seq[String] = appConfig.whitelistedIps
  override lazy val destination: Call = Call("GET", appConfig.shutterPage)
  override lazy val excludedPaths: Seq[Call] = appConfig.whitelistExcludedPaths

  override def apply(requestFunc: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    if(appConfig.whitelistEnabled) super.apply(requestFunc)(requestHeader) else requestFunc(requestHeader)
  }
}
