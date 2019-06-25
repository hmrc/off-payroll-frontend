/*
 * Copyright 2019 HM Revenue & Customs
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
import org.scalamock.scalatest.MockFactory
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.Html
import services.OptimisedDecisionService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

trait MockOptimisedDecisionService extends MockFactory {

  val mockOptimisedDecisionService = mock[OptimisedDecisionService]

//  def mockDetermineResultView(call: Call)(response: Either[Html, Html]): Unit = {
//    (mockOptimisedDecisionService.determineResultView(_: Call)( _: DataRequest[_],_: HeaderCarrier, _: Messages))
//      .expects(call, *, *, *)
//      .returns(Future.successful(response))
//  }
}
