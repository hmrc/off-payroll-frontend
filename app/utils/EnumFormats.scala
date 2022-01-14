/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package utils

import play.api.libs.json._

import scala.language.implicitConversions

trait EnumFormats {
  def enumRead[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) => {
        try {
          JsSuccess(enum.withName(s))
        } catch {
          case _: NoSuchElementException => JsError(s"Expected an enumeration of type: '${enum.getClass}'")
        }
      }
      case _ => JsError("Expected a string value")
    }
  }

  implicit def enumWrite[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }

  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumRead(enum), enumWrite)
  }
}
