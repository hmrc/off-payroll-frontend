/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import play.api.libs.json.{Json, OFormat}

case class ErrorResponse(status: Int, error: String)

object ErrorResponse{
  implicit val format: OFormat[ErrorResponse] = Json.format[ErrorResponse]
}
