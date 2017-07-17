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
//import uk.gov.hmrc.offpayroll.FrontendLogInterviewConnector
import uk.gov.hmrc.offpayroll.models.{DecisionResponse, LogInterview}
import uk.gov.hmrc.offpayroll.modelsFormat._
import uk.gov.hmrc.play.http.{HeaderCarrier, HttpPost, HttpResponse}
//import uk.gov.hmrc.offpayroll.models.InterviewFormat._
//import play.api.mvc.Result

import scala.concurrent.Future

/**
  * Created by brian on 10/07/2017.
  */
//@ImplementedBy(classOf[FrontendLogInterviewConnector])
trait LogInterviewConnector {


  val decisionURL: String
  val serviceURL: String
  val http: HttpPost

  def log(logInterview: LogInterview)(implicit hc: HeaderCarrier): Future[DecisionResponse] = {
    http.POST[LogInterview, DecisionResponse](s"$decisionURL/$serviceURL/", logInterview)
  }

}

