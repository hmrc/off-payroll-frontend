/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package connectors.httpParsers

import akka.util.ByteString
import play.api.Logger
import play.api.http.Status
import play.api.libs.ws.WSResponse

object PDFGeneratorHttpParser {

  type Response = Either[ErrorResponse, SuccessResponse]

  val reads: WSResponse => Response = response =>
    response.status match {
      case Status.OK => Right(SuccessfulPDF(response.bodyAsBytes))
      case Status.BAD_REQUEST =>
        Logger.debug(s"[PDFGeneratorHttpParser][updateReads]: Bad Request returned from PDF Generator Service:\n\n ${response.body}")
        Logger.warn(s"[PDFGeneratorHttpParser][updateReads]: Bad Request returned from PDF Generator Service")
        Left(BadRequest)
      case status =>
        Logger.debug(s"[PDFGeneratorHttpParser][updateReads]: $status returned from PDF Generator Service:\n\n $response")
        Logger.warn(s"[PDFGeneratorHttpParser][updateReads]: $status returned from PDF Generator Service")
        Left(UnexpectedError(status))
    }

  sealed trait ErrorResponse {
    val status: Int = Status.INTERNAL_SERVER_ERROR
    val body: String
  }
  case object BadRequest extends ErrorResponse {
    override val status: Int = Status.BAD_REQUEST
    override val body: String = "Bad Request returned from PDF Generator Service"
  }
  case class UnexpectedError(override val status: Int,
                             override val body: String = "Unexpected Response returned from PDF Generator Service") extends ErrorResponse

  sealed trait SuccessResponse
  case class SuccessfulPDF(pdf: ByteString) extends SuccessResponse

}
