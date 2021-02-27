/*
 * Copyright 2021 HM Revenue & Customs
 *
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