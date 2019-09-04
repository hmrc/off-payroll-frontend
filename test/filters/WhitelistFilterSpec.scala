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

package filters

import base.GuiceAppSpecBase
import connectors.{DataCacheConnector, FakeDataCacheConnector}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results.Ok
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.{Application, Configuration}

import scala.concurrent.{ExecutionContext, Future}

class WhitelistFilterSpec extends GuiceAppSpecBase {

  object TestAction extends ActionBuilder[Request, AnyContent] {
    override implicit protected def executionContext: ExecutionContext = messagesControllerComponents.executionContext
    override def parser: BodyParser[AnyContent] = messagesControllerComponents.parsers.defaultBodyParser
    override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = block(request)
  }

  override implicit lazy val app: Application =
    new GuiceApplicationBuilder()
      .overrides(bind[DataCacheConnector].to[FakeDataCacheConnector])
      .configure(Configuration(
        "whitelist.enabled" -> true
      ))
      .routes({
        case ("GET", "/hello-world") => TestAction(Ok("success"))
        case _ => TestAction(Ok("err"))
      })
      .build()

  "WhitelistFilter" when {

    "supplied with a non-whitelisted IP" should {

      lazy val fakeRequest = FakeRequest("GET", "/hello-world").withHeaders(
        "True-Client-IP" -> "127.0.0.2"
      )

      Call(fakeRequest.method, fakeRequest.uri)

      lazy val Some(result) = route(app, fakeRequest)

      "return status of 303" in {
        status(result) mustBe 303
      }

      "redirect to shutter page" in {
        redirectLocation(result) mustBe Some(frontendAppConfig.shutterPage)
      }
    }

    "supplied with a whitelisted IP" should {

      lazy val fakeRequest = FakeRequest("GET", "/hello-world").withHeaders(
        "True-Client-IP" -> "127.0.0.1"
      )

      lazy val Some(result) = route(app, fakeRequest)

      "return status of 200" in {
        status(result) mustBe 200
      }

      "return success" in {
        contentAsString(result) mustBe "success"
      }
    }
  }
}