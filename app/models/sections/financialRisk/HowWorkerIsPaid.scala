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

package models.sections.financialRisk

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait HowWorkerIsPaid

object HowWorkerIsPaid extends FeatureSwitching {

  case object HourlyDailyOrWeekly extends WithName("incomeCalendarPeriods") with HowWorkerIsPaid
  case object FixedPrice extends WithName("incomeFixed") with HowWorkerIsPaid
  case object PieceRate extends WithName("incomePieceRate") with HowWorkerIsPaid
  case object Commission extends WithName("incomeCommission") with HowWorkerIsPaid
  case object ProfitOrLosses extends WithName("incomeProfitOrLosses") with HowWorkerIsPaid

  val values: Seq[HowWorkerIsPaid] = Seq(
    HourlyDailyOrWeekly, FixedPrice, PieceRate, Commission, ProfitOrLosses
  )

  def options(implicit frontendAppConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value => RadioOption(
      keyPrefix = "howWorkerIsPaid",
      option = value.toString,
      optionType = Radio,
      hasTailoredMsgs = true,
      dividerPrefix = false)
  }

  implicit val enumerable: Enumerable[HowWorkerIsPaid] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object HowWorkerIsPaidWrites extends Writes[HowWorkerIsPaid] {
    def writes(howWorkerIsPaid: HowWorkerIsPaid) = Json.toJson(howWorkerIsPaid.toString)
  }

  implicit object HowWorkerIsPaidReads extends Reads[HowWorkerIsPaid] {
    override def reads(json: JsValue): JsResult[HowWorkerIsPaid] = json match {
      case JsString(HourlyDailyOrWeekly.toString) => JsSuccess(HourlyDailyOrWeekly)
      case JsString(FixedPrice.toString) => JsSuccess(FixedPrice)
      case JsString(PieceRate.toString) => JsSuccess(PieceRate)
      case JsString(Commission.toString) => JsSuccess(Commission)
      case JsString(ProfitOrLosses.toString) => JsSuccess(ProfitOrLosses)
      case _                          => JsError("Unknown howWorkerIsPaid")
    }
  }
}
