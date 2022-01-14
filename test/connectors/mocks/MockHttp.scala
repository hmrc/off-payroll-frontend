/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors.mocks

import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Writes
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads}
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

trait MockHttp extends MockFactory {

  val mockHttp: HttpClient = mock[HttpClient]

  def setupMockHttpPost[I,O](url: String, model: I)(response: Future[O]): Unit = {
    (mockHttp.POST(_: String, _: I, _: Seq[(String, String)])(_: Writes[I], _: HttpReads[O], _: HeaderCarrier, _: ExecutionContext))
      .expects(url, model, *, *, *, *, *)
      .returns(response)
  }
}