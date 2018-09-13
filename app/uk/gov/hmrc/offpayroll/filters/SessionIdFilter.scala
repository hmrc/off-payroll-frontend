/*
 * Copyright 2018 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.filters

import java.util.UUID

import javax.inject.{Inject, Singleton}
import akka.stream.Materializer
import play.Logger
import play.api.http.DefaultHttpFilters

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import uk.gov.hmrc.offpayroll.filters.SessionIdFilter._

object SessionIdFilter {
val OPF_SESSION_ID_COOKIE = "ofpSessionId"
 def createSessionIdCookie = Cookie(name = OPF_SESSION_ID_COOKIE, value = s"opf-session-${UUID.randomUUID}")
}

@Singleton
class SessionIdFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter {
    def apply(nextFilter: RequestHeader => Future[Result])
             (requestHeader: RequestHeader): Future[Result] = {
      nextFilter(requestHeader).map { result =>
        requestHeader.cookies.find(_.name == SessionIdFilter.OPF_SESSION_ID_COOKIE).map(_.value) match {
          case Some(sessionId) =>
            Logger.debug(s"session id filter: found session id $sessionId in cookie $OPF_SESSION_ID_COOKIE")
            result
          case None =>
            val sessionId = createSessionIdCookie
            Logger.debug(s"session id filter: created session id $sessionId and stored in cookie $OPF_SESSION_ID_COOKIE")
            result.withCookies(sessionId)
        }
      }
    }
}

@Singleton
class OffPayrollFrontendFilters @Inject() (sessionIdFilter: SessionIdFilter) extends DefaultHttpFilters(sessionIdFilter)
