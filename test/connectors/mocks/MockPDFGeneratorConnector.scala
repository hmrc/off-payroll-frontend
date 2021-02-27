/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors.mocks

import connectors.PDFGeneratorConnector
import connectors.httpParsers.PDFGeneratorHttpParser
import org.scalamock.scalatest.MockFactory
import play.twirl.api.Html
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockPDFGeneratorConnector extends MockFactory {

  val mockPdfGeneratorConnector: PDFGeneratorConnector = mock[PDFGeneratorConnector]

  def mockGeneratePdf(content: Html)(response: Future[PDFGeneratorHttpParser.Response]): Unit = {
    (mockPdfGeneratorConnector.generatePdf(_: Html)(_: HeaderCarrier, _: ExecutionContext))
      .expects(content, *, *)
      .returns(response)
  }
}