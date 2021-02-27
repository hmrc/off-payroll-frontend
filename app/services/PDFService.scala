/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services

import connectors.PDFGeneratorConnector
import connectors.httpParsers.PDFGeneratorHttpParser.Response
import javax.inject.Inject
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class PDFService @Inject()(pdfGeneratorConnector: PDFGeneratorConnector) {

  def generatePdf(content: Html)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Response] =
    pdfGeneratorConnector.generatePdf(content)

}
