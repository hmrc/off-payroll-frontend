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
import viewmodels.RadioOption

sealed trait HowWorkIsDone

object HowWorkIsDone {

  case object Noworkerinputallowed extends WithName("noWorkerInputAllowed") with HowWorkIsDone
  case object Workerdecideswithoutinput extends WithName("workerDecidesWithoutInput") with HowWorkIsDone
  case object Workerfollowstrictemployeeprocedures extends WithName("workerFollowStrictEmployeeProcedures") with HowWorkIsDone
  case object Workeragreewithothers extends WithName("workerAgreeWithOthers") with HowWorkIsDone

  val values: Seq[HowWorkIsDone] = Seq(
    Noworkerinputallowed, Workerdecideswithoutinput, Workerfollowstrictemployeeprocedures, Workeragreewithothers
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("howWorkIsDone", value.toString)
  }

  implicit val enumerable: Enumerable[HowWorkIsDone] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object HowWorkIsDoneWrites extends Writes[HowWorkIsDone] {
    def writes(howWorkIsDone: HowWorkIsDone) = Json.toJson(howWorkIsDone.toString)
  }

  implicit object HowWorkIsDoneReads extends Reads[HowWorkIsDone] {
    override def reads(json: JsValue): JsResult[HowWorkIsDone] = json match {
      case JsString(Noworkerinputallowed.toString) => JsSuccess(Noworkerinputallowed)
      case JsString(Workerdecideswithoutinput.toString) => JsSuccess(Workerdecideswithoutinput)
      case JsString(Workerfollowstrictemployeeprocedures.toString) => JsSuccess(Workerfollowstrictemployeeprocedures)
      case JsString(Workeragreewithothers.toString) => JsSuccess(Workeragreewithothers)
      case _                          => JsError("Unknown howWorkIsDone")
    }
  }
}
