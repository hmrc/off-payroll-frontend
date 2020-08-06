/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors.httpParsers

import models.{DecisionResponse, ErrorResponse}
import play.api.Logging
import play.mvc.Http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

class DecisionHttpParser extends Logging

object DecisionHttpParser {

  implicit object DecisionReads extends HttpReads[Either[ErrorResponse, DecisionResponse]] with Logging {

    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, DecisionResponse] = {

      response.status match {
        case OK => response.json.validate[DecisionResponse].fold(
          invalid => {
            logger.error(s"[DecisionHttpParser][DecisionReads] Invalid Json received from decision connector from decision API. $invalid")
            Left(ErrorResponse(INTERNAL_SERVER_ERROR, "Invalid Json received from decision API")): Either[ErrorResponse, DecisionResponse]
          }
          ,validJson => {
            logger.debug(s"[DecisionHttpParser][DecisionReads] Decision service response: $validJson")
            Right(validJson)
          }
        )
        case unexpectedStatus@_ =>
          logger.error(s"[DecisionHttpParser][DecisionReads] Unexpected response from decision API - Response: $unexpectedStatus")
          Left(ErrorResponse(response.status,"Unexpected Response returned from decision API"))
      }
    }
  }
}
