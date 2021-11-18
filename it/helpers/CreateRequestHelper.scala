package helpers

import config.SessionKeys
import org.scalatestplus.play.ServerProvider
import play.api.Application
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.libs.ws.{DefaultWSCookie, WSClient, WSCookie, WSRequest, WSResponse}
import play.api.mvc.Cookie

import scala.concurrent.Future
import scala.concurrent.duration.{Duration, FiniteDuration, SECONDS}
import models.sections.setup.WhoAreYou
import models.sections.setup.WhoAreYou.Worker

trait CreateRequestHelper extends ServerProvider {
  
  val defaultSeconds = 5
  val defaultDuration: FiniteDuration = Duration.apply(defaultSeconds, SECONDS)

  val app: Application

  lazy val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  def wsCookie(c: Cookie): WSCookie =
    DefaultWSCookie(
      name = c.name,
      value = c.value,
      domain = c.domain,
      path = Some(c.path),
      maxAge = c.maxAge.map(_.toLong),
      secure = c.secure,
      httpOnly = c.httpOnly
    )

  def buildSessionRequest(path: String, followRedirect: Boolean = false): WSRequest = {
    ws.url(s"http://localhost:$port/check-employment-status-for-tax$path")
      .withHttpHeaders(HeaderNames.COOKIE -> SessionCookieBaker.bakeSessionCookie(Map(SessionKeys.userType -> Json.toJson[WhoAreYou](Worker).toString)))
      .withFollowRedirects(followRedirect)
  }

  def getRequest(path: String, followRedirect: Boolean = true): Future[WSResponse] =
    buildSessionRequest(path, followRedirect).get()

  def getSessionRequest(path: String, followRedirect: Boolean = true)(implicit cookies: Seq[WSCookie]): Future[WSResponse] =
    buildSessionRequest(path, followRedirect)
      .addHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .get()

  def postRequest(path: String, formString: String, followRedirect: Boolean = false): Future[WSResponse] =
    buildSessionRequest(path, followRedirect)
      .addHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded", "Csrf-Token" -> "nocheck")
      .post(formString)

  def postSessionRequest(path: String, formString: String, followRedirect: Boolean = false)(implicit  cookies: Seq[WSCookie]): Future[WSResponse] =
    buildSessionRequest(path, followRedirect)
      .withCookies(cookies: _*)
      .addHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded", "Csrf-Token" -> "nocheck")
      .post(formString)

  def optionsRequest(path: String, followRedirect: Boolean = false): Future[WSResponse] =
    buildSessionRequest(path, followRedirect)
      .options()

}
