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

package models.sections.setup

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait WorkerType

object WorkerType {

  case object LimitedCompany extends WithName("limitedCompany") with WorkerType
  case object SoleTrader extends WithName("soleTrader") with WorkerType

  val values: Seq[WorkerType] = Seq(LimitedCompany, SoleTrader)

  implicit object WorkerTypeWrites extends Writes[WorkerType] {
    def writes(workerType: WorkerType) = Json.toJson(workerType.toString)
  }

  implicit object WorkerTypeReads extends Reads[WorkerType] {
    override def reads(json: JsValue): JsResult[WorkerType] = json match {
      case JsString(LimitedCompany.toString) => JsSuccess(LimitedCompany)
      case JsString(SoleTrader.toString) => JsSuccess(SoleTrader)
      case _                          => JsError("Unknown workerType")
    }
  }
}
