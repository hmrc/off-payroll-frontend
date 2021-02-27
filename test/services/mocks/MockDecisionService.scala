/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package services.mocks

import models.requests.DataRequest
import models.{AdditionalPdfDetails, DecisionResponse, ErrorResponse}
import org.scalamock.scalatest.MockFactory
import play.api.data.Form
import play.api.i18n.Messages
import play.twirl.api.Html
import services.DecisionService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{AnswerSection, ResultMode}

import scala.concurrent.Future

trait MockDecisionService extends MockFactory {

  val mockDecisionService = mock[DecisionService]

  def mockDecide(response: Either[ErrorResponse, DecisionResponse]): Unit = {
    (mockDecisionService.decide(_: DataRequest[_], _: HeaderCarrier))
      .expects(*, *)
      .returns(Future.successful(response))
  }

  def mockDetermineResultView(decisionResponse: DecisionResponse, form : Option[Form[Boolean]] = None)(response: Either[Html, Html]): Unit = {
    (mockDecisionService.determineResultView(
      _: DecisionResponse,
      _: Option[Form[Boolean]],
      _: Seq[AnswerSection],
      _: ResultMode,
      _: Option[AdditionalPdfDetails],
      _: Option[String],
      _: Option[String]
    )( _: DataRequest[_],_: HeaderCarrier, _: Messages))
      .expects(decisionResponse, form, *, *, *, *, *, *, *, *)
      .returns(response)
  }
}
