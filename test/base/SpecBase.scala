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
import config.featureSwitch.{FeatureSwitching, TailoredContent}
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import handlers.ErrorHandler
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.Wiremock

import scala.concurrent.duration.{Duration, FiniteDuration, _}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.implicitConversions


trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterEach with MaterializerSupport with MockitoSugar with FeatureSwitching {

  override def fakeApplication(): Application = GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
    .build()

  override def beforeEach(): Unit = {
    enable(TailoredContent)
    super.beforeEach()
  }

  implicit val defaultTimeout: FiniteDuration = 5.seconds

  implicit def extractAwait[A](future: Future[A]): A = await[A](future)

  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  lazy val injector = app.injector

  implicit def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit def lang: Lang = Lang("en")

  def messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  implicit def ec: ExecutionContext = injector.instanceOf[ExecutionContext]
  implicit def hc: HeaderCarrier = HeaderCarrier()

  def errorHandler = injector.instanceOf[ErrorHandler]

  implicit def fakeRequest = FakeRequest("", "")

  implicit def messages: Messages = messagesApi.preferred(fakeRequest)

  val stubPort = 8080
  val wireMock = new Wiremock

  val client = injector.instanceOf[HttpClient]
  val servicesConfig = mock[ServicesConfig]

  when(servicesConfig.baseUrl(any())).thenReturn(s"http://localhost:$stubPort")

}
