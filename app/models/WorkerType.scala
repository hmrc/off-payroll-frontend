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
import viewmodels.{RadioOption, radio}

sealed trait WorkerType

object WorkerType {

  case object LimitedCompany extends WithName("limitedCompany") with WorkerType
  case object Partnership extends WithName("partnership") with WorkerType
  case object ThroughIndividual extends WithName("throughIndividual") with WorkerType
  case object SoleTrader extends WithName("soleTrader") with WorkerType

  val values: Seq[WorkerType] = Seq(
    LimitedCompany, Partnership, ThroughIndividual, SoleTrader
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("workerType", value.toString, radio)
  }

  implicit val enumerable: Enumerable[WorkerType] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object WorkerTypeWrites extends Writes[WorkerType] {
    def writes(workerType: WorkerType) = Json.toJson(workerType.toString)
  }

  implicit object WorkerTypeReads extends Reads[WorkerType] {
    override def reads(json: JsValue): JsResult[WorkerType] = json match {
      case JsString(LimitedCompany.toString) => JsSuccess(LimitedCompany)
      case JsString(Partnership.toString) => JsSuccess(Partnership)
      case JsString(ThroughIndividual.toString) => JsSuccess(ThroughIndividual)
      case JsString(SoleTrader.toString) => JsSuccess(SoleTrader)
      case _                          => JsError("Unknown workerType")
    }
  }
}
