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

import MultiDecision.Result
import cats.data.EitherT
import cats.implicits._
import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import handlers.ErrorHandler
import models.{DecisionResponse, ErrorResponse}
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.Wiremock

import scala.concurrent.duration.{Duration, FiniteDuration, _}
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.implicitConversions


trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterEach with MaterializerSupport with FeatureSwitching {

  override lazy val app: Application = GuiceApplicationBuilder()
    .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
    .build()

  override def beforeEach(): Unit = {
    disable(OptimisedFlow)
    super.beforeEach()
  }

  def title(heading: String, section: Option[String] = None)(implicit messages: Messages) =
    s"$heading - ${section.fold("")(_ + " - ")}${messages("site.service_name")} - ${messages("site.govuk")}"

  implicit val defaultTimeout: FiniteDuration = 5.seconds

  implicit def extractAwait[A](future: Future[A]): A = await[A](future)

  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  lazy val injector = app.injector

  implicit lazy val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit lazy val lang: Lang = Lang("en")

  lazy val messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]
  implicit lazy val hc: HeaderCarrier = HeaderCarrier()

  val errorHandler = injector.instanceOf[ErrorHandler]

  implicit lazy val fakeRequest = FakeRequest("", "")

  implicit lazy val messages: Messages = messagesApi.preferred(fakeRequest)

  val wireMock = new Wiremock

  val client = injector.instanceOf[HttpClient]

  def createRightType(value: Boolean): Result[Boolean] = EitherT.right(Future(value))
  def createLeftType(value: Either[ErrorResponse,DecisionResponse]): Result[Boolean] = EitherT.left(Future(value))

}
