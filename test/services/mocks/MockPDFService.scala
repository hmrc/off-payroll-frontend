/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services.mocks

import connectors.httpParsers.PDFGeneratorHttpParser
import org.scalamock.scalatest.MockFactory
import play.twirl.api.Html
import services.PDFService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockPDFService extends MockFactory {

  val mockPDFService = mock[PDFService]

  def mockGeneratePdf(response: PDFGeneratorHttpParser.Response): Unit = {
    (mockPDFService.generatePdf(_: Html)(_: HeaderCarrier, _: ExecutionContext))
      .expects(*, *, *)
      .returns(Future.successful(response))
  }
}
