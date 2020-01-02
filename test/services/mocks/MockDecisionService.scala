/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
