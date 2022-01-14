/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package base

import config.FrontendAppConfig
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import handlers.ErrorHandler
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, inject}
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.{Injector, bind}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.webchat.client.WebChatClient

import scala.concurrent.ExecutionContext


trait GuiceAppSpecBase extends SpecBase with GuiceOneAppPerSuite with BeforeAndAfterEach {

  override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector],
      inject.bind[WebChatClient].toInstance(new uk.gov.hmrc.webchat.testhelpers.WebChatClientStub))
    .build()

  lazy val injector: Injector = app.injector

  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  implicit lazy val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  lazy val messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]
  lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]
  implicit lazy val messages: Messages = messagesApi.preferred(Seq(lang))

  val errorHandler: ErrorHandler = injector.instanceOf[ErrorHandler]

}
