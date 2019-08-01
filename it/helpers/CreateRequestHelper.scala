package helpers

import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.http.HeaderNames
import play.api.libs.json.JsValue
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSRequest, WSResponse}
import play.api.mvc.{Cookies, Headers, RequestHeader, Session}
import uk.gov.hmrc.http.SessionKeys
import uk.gov.hmrc.http.{HeaderNames => HMRCHeaderNames}

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}

trait CreateRequestHelper extends ServerProvider {

  val defaultSeconds = 5
  val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  lazy val sessionId = "id"

  val cookies: String = {
    val session = Session(Map(SessionKeys.sessionId -> sessionId))
    val cookies = Seq(Session.encodeAsCookie(session))
    Cookies.encodeCookieHeader(cookies)
  }

  def buildRequest(path: String, followRedirect: Boolean = false): WSRequest = {
    ws.url(s"http://localhost:$port$path")
      .withHttpHeaders(HMRCHeaderNames.xSessionId -> sessionId, HeaderNames.COOKIE -> cookies)
      .withFollowRedirects(followRedirect)
  }

  def getRequest(path: String, followRedirect: Boolean = false): Future[WSResponse] =
    buildRequest(path, followRedirect).get()

  def postRequest(path: String, formJson: JsValue, followRedirect: Boolean = true): Future[WSResponse] =
    buildRequest(path, followRedirect).post(formJson)

}
