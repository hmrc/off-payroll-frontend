/*
 * Copyright 2020 HM Revenue & Customs
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
