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

package models

import play.api.libs.json._
import viewmodels.{RadioOption, checkbox}

sealed trait BusinessSize

object BusinessSize {

  case object Turnover extends WithName("turnover") with BusinessSize
  case object Balancesheet extends WithName("balanceSheet") with BusinessSize
  case object Employees extends WithName("employees") with BusinessSize
  case object Noneofabove extends WithName("noneOfAbove") with BusinessSize

  val values: Seq[BusinessSize] = Seq(
    Turnover, Balancesheet, Employees, Noneofabove
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("businessSize", value.toString, checkbox)
  }

  implicit val enumerable: Enumerable[BusinessSize] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object BusinessSizeWrites extends Writes[BusinessSize] {
    def writes(businessSize: BusinessSize) = Json.toJson(businessSize.toString)
  }

  implicit object BusinessSizeReads extends Reads[BusinessSize] {
    override def reads(json: JsValue): JsResult[BusinessSize] = json match {
      case JsString(Turnover.toString) => JsSuccess(Turnover)
      case JsString(Balancesheet.toString) => JsSuccess(Balancesheet)
      case JsString(Employees.toString) => JsSuccess(Employees)
      case JsString(Noneofabove.toString) => JsSuccess(Noneofabove)
      case _                          => JsError("Unknown businessSize")
    }
  }
}
