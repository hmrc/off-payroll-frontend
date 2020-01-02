/*
 * Copyright 2020 HM Revenue & Customs
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

package utils

import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{RequestHeader, Result, Session}

import scala.util.Try

object SessionUtils {

  implicit class SessionUtils(session: Session) {
    def getModel[T](key: String)(implicit sessionFormatter: SessionFormatter[T]): Option[T] =
      session.get(key) flatMap sessionFormatter.fromString
  }

  implicit class ResultUtils(result: Result) {
    def addingToSession[T](values: (String, T)*)(implicit sessionFormatter: SessionFormatter[T], requestHeader: RequestHeader): Result = {
      val transformed = values.map {
        case (a,b) => (a, sessionFormatter.toString(b))
      }
      result.addingToSession(transformed: _*)
    }
  }

  trait SessionFormatter[T] {
    def toString(entity: T): String
    def fromString(string: String): Option[T]
  }

  implicit def jsonSessionFormatter[T](implicit reader: Reads[T], writer: Writes[T]): SessionFormatter[T] = new SessionFormatter[T] {
    override def toString(entity: T): String = (writer writes entity).toString

    override def fromString(string: String): Option[T] = for {
      json <- Try(Json.parse(string)).toOption
      parsedJson <- json.asOpt[T]
    } yield parsedJson
  }
}
