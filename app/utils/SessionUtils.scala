/*
 * Copyright 2020 HM Revenue & Customs
 *
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
