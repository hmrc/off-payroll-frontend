/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package base

import config.FrontendAppConfig
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import handlers.ErrorHandler
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.MessagesControllerComponents

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions


trait GuiceAppSpecBase extends SpecBase with GuiceOneAppPerSuite {

  override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
    .build()

  lazy val injector = app.injector

  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  implicit lazy val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  lazy val messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]
  lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit lazy val messages: Messages = messagesApi.preferred(Seq(lang))

  val errorHandler = injector.instanceOf[ErrorHandler]

}
