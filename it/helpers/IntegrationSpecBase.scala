package helpers

import config.FrontendAppConfig
import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.http.HeaderNames
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSResponse
import play.modules.reactivemongo.ReactiveMongoComponent
import repositories.SessionRepository

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper
  with GuiceOneServerPerSuite
  with BeforeAndAfterEach with BeforeAndAfterAll with Eventually {

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure(Map("play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck"))
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
