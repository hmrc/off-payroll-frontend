/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors.httpParsers

import models.ErrorResponse
import play.api.Logger
import play.mvc.Http.Status.NO_CONTENT
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object LogHttpParser {

  implicit object LogReads extends HttpReads[Either[ErrorResponse, Boolean]] {

    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, Boolean] = {
      response.status match {
        case NO_CONTENT => Right(true)
        case unexpectedStatus@_ =>
          Logger.error(s"[LogHttpParser][LogReads] Unexpected response from log API - Response: $unexpectedStatus: ${response.body}")
          Left(ErrorResponse(response.status,"Unexpected Response returned from log API"))
      }
    }
  }
}
