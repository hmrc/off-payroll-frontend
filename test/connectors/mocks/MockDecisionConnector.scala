/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package connectors.mocks

import connectors.DecisionConnector
import models.{DecisionResponse, ErrorResponse, Interview}
import org.scalamock.scalatest.MockFactory
import play.api.libs.json.Writes
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockDecisionConnector extends MockFactory {

  lazy val mockDecisionConnector = mock[DecisionConnector]

  def mockDecide(decisionRequest: Interview, writes: Writes[Interview] = Interview.writes)(response: Either[ErrorResponse, DecisionResponse]): Unit = {
    (mockDecisionConnector.decide(_: Interview, _: Writes[Interview])(_: HeaderCarrier, _: ExecutionContext))
      .expects(decisionRequest, *,  *, *)
      .returns(Future.successful(response))
  }
}