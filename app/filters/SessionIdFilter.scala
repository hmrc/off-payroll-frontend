/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package filters

import java.util.UUID

import akka.stream.Materializer
import com.google.inject.Inject
import play.api.http.HeaderNames
import play.api.mvc._
import uk.gov.hmrc.http.{SessionKeys, HeaderNames => HMRCHeaderNames}
import play.api.mvc.SessionCookieBaker

import scala.concurrent.{ExecutionContext, Future}

class SessionIdFilter (
                        override val mat: Materializer,
                        uuid: => UUID,
                        implicit val ec: ExecutionContext,
                        val cookieBaker: SessionCookieBaker,
                        cookieHeaderEncoding: CookieHeaderEncoding
                      ) extends Filter {

  @Inject
  def this(mat: Materializer, ec: ExecutionContext, cb: SessionCookieBaker, che: CookieHeaderEncoding) {
    this(mat, UUID.randomUUID(), ec, cb, che)
  }

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {

    lazy val sessionId: String = s"session-$uuid"

    if (rh.session.get(SessionKeys.sessionId).isEmpty) {

      val cookies: String = {
        val session: Session = rh.session + (SessionKeys.sessionId -> sessionId)
        val cookies: Traversable[Cookie] = rh.cookies ++ Seq(cookieBaker.encodeAsCookie(session))
        cookieHeaderEncoding.encodeCookieHeader(cookies.toSeq)
      }

      val headers = Headers(
        HMRCHeaderNames.xSessionId -> sessionId,
        HeaderNames.COOKIE -> cookies
      )

      f(rh.withHeaders(headers)).map(_.addingToSession(SessionKeys.sessionId -> sessionId)(rh.withHeaders(headers)))
    } else {
      f(rh)
    }
  }
}
