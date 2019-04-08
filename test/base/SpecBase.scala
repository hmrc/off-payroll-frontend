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

package base

import config.FrontendAppConfig
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest

import scala.concurrent.ExecutionContext

trait SpecBase extends PlaySpec with MaterializerSupport {

  val injector = new GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
    .injector()

  implicit def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit def lang: Lang = Lang("en")

  def messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  implicit def ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  def fakeRequest = FakeRequest("", "")

  implicit def messages: Messages = messagesApi.preferred(fakeRequest)
}
