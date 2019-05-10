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

sealed trait ChooseWhereWork

object ChooseWhereWork {

  case object Workerchooses extends WithName("workerChooses") with ChooseWhereWork
  case object Workercannotchoose extends WithName("workerCannotChoose") with ChooseWhereWork
  case object Nolocationrequired extends WithName("noLocationRequired") with ChooseWhereWork
  case object Workeragreewithothers extends WithName("workerAgreeWithOthers") with ChooseWhereWork

  val values: Seq[ChooseWhereWork] = Seq(
    Workerchooses, Workercannotchoose, Nolocationrequired, Workeragreewithothers
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("chooseWhereWork", value.toString, radio)
  }

  implicit val enumerable: Enumerable[ChooseWhereWork] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ChooseWhereWorkWrites extends Writes[ChooseWhereWork] {
    def writes(chooseWhereWork: ChooseWhereWork) = Json.toJson(chooseWhereWork.toString)
  }

  implicit object ChooseWhereWorkReads extends Reads[ChooseWhereWork] {
    override def reads(json: JsValue): JsResult[ChooseWhereWork] = json match {
      case JsString(Workerchooses.toString) => JsSuccess(Workerchooses)
      case JsString(Workercannotchoose.toString) => JsSuccess(Workercannotchoose)
      case JsString(Nolocationrequired.toString) => JsSuccess(Nolocationrequired)
      case JsString(Workeragreewithothers.toString) => JsSuccess(Workeragreewithothers)
      case _                          => JsError("Unknown chooseWhereWork")
    }
  }
}
