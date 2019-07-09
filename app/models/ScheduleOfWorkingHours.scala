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
import viewmodels.{RadioOption, Radio}

sealed trait ScheduleOfWorkingHours

object ScheduleOfWorkingHours {

  case object ScheduleDecidedForWorker extends WithName("scheduleDecidedForWorker") with ScheduleOfWorkingHours
  case object WorkerDecideSchedule extends WithName("workerDecideSchedule") with ScheduleOfWorkingHours
  case object WorkerAgreeSchedule extends WithName("workerAgreeSchedule") with ScheduleOfWorkingHours
  case object NoScheduleRequiredOnlyDeadlines extends WithName("noScheduleRequiredOnlyDeadlines") with ScheduleOfWorkingHours

  val values: Seq[ScheduleOfWorkingHours] = Seq(
    ScheduleDecidedForWorker, WorkerDecideSchedule, WorkerAgreeSchedule, NoScheduleRequiredOnlyDeadlines
  )

  def options(optimised: Boolean = false): Seq[RadioOption] = values.map {
    value =>

      if(optimised){
        RadioOption("optimised.scheduleOfWorkingHours", value.toString, Radio, hasTailoredMsgs = true)
      } else {
        RadioOption("scheduleOfWorkingHours", value.toString, Radio, hasTailoredMsgs = true)
      }
  }

  implicit val enumerable: Enumerable[ScheduleOfWorkingHours] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ScheduleOfWorkingHoursWrites extends Writes[ScheduleOfWorkingHours] {
    def writes(scheduleOfWorkingHours: ScheduleOfWorkingHours) = Json.toJson(scheduleOfWorkingHours.toString)
  }

  implicit object ScheduleOfWorkingHoursReads extends Reads[ScheduleOfWorkingHours] {
    override def reads(json: JsValue): JsResult[ScheduleOfWorkingHours] = json match {
      case JsString(ScheduleDecidedForWorker.toString) => JsSuccess(ScheduleDecidedForWorker)
      case JsString(WorkerDecideSchedule.toString) => JsSuccess(WorkerDecideSchedule)
      case JsString(WorkerAgreeSchedule.toString) => JsSuccess(WorkerAgreeSchedule)
      case JsString(NoScheduleRequiredOnlyDeadlines.toString) => JsSuccess(NoScheduleRequiredOnlyDeadlines)
      case _                          => JsError("Unknown scheduleOfWorkingHours")
    }
  }
}
