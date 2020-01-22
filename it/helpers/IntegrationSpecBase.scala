package helpers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import org.jsoup.Jsoup
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.http.{HeaderNames, Status}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSCookie, WSResponse}
import play.modules.reactivemongo.ReactiveMongoComponent
import repositories.SessionRepository

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper
  with GuiceOneServerPerSuite
  with BeforeAndAfterEach with BeforeAndAfterAll with Eventually with CreateRequestHelper with Status with TestData with FeatureSwitching {

  def titleOf(response: WSResponse): String = Jsoup.parse(response.body).title

  implicit lazy val cookies: Seq[WSCookie] = whenReady(getRequest("/disclaimer", followRedirect = true))(_.cookies)

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
      "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
      "microservice.services.cest-decision.host" -> WiremockHelper.wiremockHost,
      "microservice.services.cest-decision.port" -> WiremockHelper.wiremockPort,
      "microservice.services.pdf-generator-service.host" -> WiremockHelper.wiremockHost,
      "microservice.services.pdf-generator-service.port" -> WiremockHelper.wiremockPort
    ))
    .build()

  lazy val component: ReactiveMongoComponent = app.injector.instanceOf(classOf[ReactiveMongoComponent])

  val mongo = new SessionRepository(
    mongoComponent = component,
    appConfig = app.injector.instanceOf[FrontendAppConfig],
    appName = "app"
  )

  override def beforeEach(): Unit = {
    resetWiremock()
    Await.result(mongo.drop, 10.seconds)
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll(): Unit = {
    stopWiremock()
    Await.result(mongo.drop, 10.seconds)
    super.afterAll()
  }

  def redirectLocation(response: WSResponse) = response.header(HeaderNames.LOCATION)


}
