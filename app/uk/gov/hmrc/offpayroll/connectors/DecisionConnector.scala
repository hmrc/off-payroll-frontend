/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.connectors

import com.google.inject.ImplementedBy
import play.api.Logger
import play.api.http.Status.{INTERNAL_SERVER_ERROR, NOT_FOUND}
import uk.gov.hmrc.offpayroll.FrontendDecisionConnector
import uk.gov.hmrc.offpayroll.models.{DecisionRequest, DecisionResponse, LogInterview}
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost, HttpResponse, NotFoundException}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
  * Created by peter on 12/12/2016.
  */
@ImplementedBy(classOf[FrontendDecisionConnector])
trait DecisionConnector {


  val decisionURL: String
  val serviceDecideURL: String
  val serviceLogURL: String
  val http: HttpPost

  def decide(decideRequest: DecisionRequest)(implicit hc: HeaderCarrier): Future[DecisionResponse] = {
    http.POST[DecisionRequest, DecisionResponse](s"$decisionURL/$serviceDecideURL/", decideRequest)
  }

  def log(logInterview: LogInterview)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
    http.POST[LogInterview, HttpResponse](s"$decisionURL/$serviceLogURL/", logInterview).map {
      result =>
        result
    } recover {
      case e : NotFoundException =>
        Logger.error(s"Not Found Exception while calling Offpayroll Decision to log analytics ${e.getMessage}")
        HttpResponse.apply(NOT_FOUND)
      case e : Exception =>
        Logger.error(s"Exception while connecting to Offpayroll Decision to log analytics ${e.getMessage}")
        HttpResponse.apply(INTERNAL_SERVER_ERROR)
    }
  }

}
