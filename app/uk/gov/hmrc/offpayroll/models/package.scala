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

package uk.gov.hmrc.offpayroll

import ai.x.play.json.Jsonx
import play.api.libs.json.{Format, Json, Reads, Writes}
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.offpayroll.models._
import uk.gov.hmrc.play.http.HeaderCarrier

/**
  * Created by peter on 12/12/2016.
  */
package object modelsFormat {

  val dateFormat = "yyyy-MM-dd HH:mm:ss"

  implicit val decideRequestFormatter: Format[DecisionRequest] = Json.format[DecisionRequest]
  implicit val decideResponseFormatter: Format[DecisionResponse] = Json.format[DecisionResponse]
  implicit val hc = HeaderCarrier()


  implicit val dateWrites = Writes.jodaDateWrites(dateFormat)
  implicit val dateReads = Reads.jodaDateReads(dateFormat)

  implicit val interviewSetupFormat = Json.format[Setup]
  implicit val interviewExitFormat = Json.format[Exit]
  implicit val interviewPersonalServiceFormat = Json.format[PersonalService]
  implicit val interviewControlFormat = Json.format[Control]
  implicit val interviewFinancialRiskFormat = Json.format[FinancialRisk]
  implicit val interviewPartAndParcelFormat = Json.format[PartAndParcel]
  implicit val interviewFormat = Json.format[LogInterview]

  implicit val interviewSearchFormat = Json.format[InterviewSearch]

  implicit val interviewSearchResponceFormat = Jsonx.formatCaseClass[InterviewSearchResponse]
  implicit val analyticsResponseFormat = Jsonx.formatCaseClass[AnalyticsResponse]


}



package object typeDefs {

  type Interview = Map[String, String]

}
