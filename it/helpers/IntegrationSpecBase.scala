package helpers

import config.featureSwitch.FeatureSwitching
import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.http.{HeaderNames, Status}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSCookie, WSResponse}

trait IntegrationSpecBase extends AnyWordSpec with Matchers with TestSuite with ScalaFutures with IntegrationPatience
  with GuiceOneServerPerSuite with BeforeAndAfterEach with BeforeAndAfterAll with Eventually with CreateRequestHelper
  with Status with TestData with FeatureSwitching with WiremockHelper {

  def titleOf(response: WSResponse): String = Jsoup.parse(response.body).title

  implicit lazy val cookies: Seq[WSCookie] = whenReady(getRequest("/disclaimer", true))(_.cookies)

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map(
      "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
      "microservice.services.cest-decision.host" -> WiremockHelper.wiremockHost,
      "microservice.services.cest-decision.port" -> WiremockHelper.wiremockPort,
      "microservice.services.pdf-generator-service.host" -> WiremockHelper.wiremockHost,
      "microservice.services.pdf-generator-service.port" -> WiremockHelper.wiremockPort
    ))
    .build()

  override def beforeEach(): Unit = {
    resetWiremock()
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll(): Unit = {
    stopWiremock()
    super.afterAll()
  }

  def redirectLocation(response: WSResponse) = response.header(HeaderNames.LOCATION)
}
