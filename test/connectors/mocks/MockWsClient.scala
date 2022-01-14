/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package connectors.mocks

import connectors.utils.WsUtils
import org.scalamock.scalatest.MockFactory
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}

import scala.concurrent.Future

trait MockWsClient extends WsUtils with MockFactory {

  val mockWsClient: WSClient = mock[WSClient]
  val mockWsRequest: WSRequest = mock[WSRequest]

  def setupMockHttpPost[I,O](url: String, model: I)(response: Future[WSResponse])(implicit writesI: BodyWritable[I]): Unit = {
    mockClientUrl(url)
    mockRequestPost(model)(response)
  }

  private def mockClientUrl(url: String): Unit = {
    (mockWsClient.url(_: String))
      .expects(url)
      .returns(mockWsRequest)
  }

  private def mockRequestPost[I](model: I)(response: Future[WSResponse]): Unit = {
    (mockWsRequest.post(_: I)(_: BodyWritable[I]))
      .expects(model, *)
      .returns(response)
  }
}