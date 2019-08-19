package helpers

import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.http.HeaderNames
import play.api.libs.json.JsValue
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSCookie, WSRequest, WSResponse}
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

  def buildRequest(path: String, followRedirect: Boolean = false): WSRequest = {
    ws.url(s"http://localhost:$port/check-employment-status-for-tax$path")
      .withFollowRedirects(followRedirect)
  }

  def buildSessionRequest(path: String, followRedirect: Boolean = false): WSRequest = {
    ws.url(s"http://localhost:$port/check-employment-status-for-tax$path")
      .withFollowRedirects(followRedirect)
  }

  def getRequest(path: String, followRedirect: Boolean = true): Future[WSResponse] =
    buildRequest(path, followRedirect).get()

  def getSessionRequest(path: String, followRedirect: Boolean = true)(implicit cookies: Seq[WSCookie]): Future[WSResponse] =
    buildRequest(path, followRedirect)
      .withCookies(cookies: _*)
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded").get()

  def postRequest(path: String, formString: String, followRedirect: Boolean = true): Future[WSResponse] =
    buildRequest(path, followRedirect)
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .post(formString)

  def postSessionRequest(path: String, formString: String, followRedirect: Boolean = true)(implicit  cookies: Seq[WSCookie]): Future[WSResponse] =
    buildRequest(path, followRedirect)
      .withCookies(cookies: _*)
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded", "Csrf-Token" -> "nocheck")
      .post(formString)

  def optionsRequest(path: String, followRedirect: Boolean = true): Future[WSResponse] =
    buildRequest(path, followRedirect)
      .options()

}
