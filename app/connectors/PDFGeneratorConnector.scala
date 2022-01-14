/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors

import config.FrontendAppConfig
import connectors.httpParsers.PDFGeneratorHttpParser
import connectors.httpParsers.PDFGeneratorHttpParser.Response
import javax.inject.Inject
import models.PdfRequest
import play.api.Logging
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}


class PDFGeneratorConnector @Inject()(ws: WSClient,
                                      appConfig: FrontendAppConfig) extends Logging {

  private[connectors] lazy val url = appConfig.pdfGeneratorService + "/pdf-generator-service/generate"

  def generatePdf(html: Html)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Response] = {
    logger.debug(s"[PDFGeneratorConnector][generatePdf] PDF HTML:\n\n$html")
    ws.url(url).post(Json.toJson(PdfRequest(html))) map PDFGeneratorHttpParser.reads
  }
}
