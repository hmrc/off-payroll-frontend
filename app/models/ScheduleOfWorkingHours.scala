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

sealed trait ScheduleOfWorkingHours

object ScheduleOfWorkingHours {

  case object Scheduledecidedforworker extends WithName("scheduleDecidedForWorker") with ScheduleOfWorkingHours
  case object Workerdecideschedule extends WithName("workerDecideSchedule") with ScheduleOfWorkingHours
  case object WorkerAgreeSchedule extends WithName("workerAgreeSchedule") with ScheduleOfWorkingHours
  case object Noschedulerequiredonlydeadlines extends WithName("noScheduleRequiredOnlyDeadlines") with ScheduleOfWorkingHours

  val values: Seq[ScheduleOfWorkingHours] = Seq(
    Scheduledecidedforworker, Workerdecideschedule, WorkerAgreeSchedule, Noschedulerequiredonlydeadlines
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("scheduleOfWorkingHours", value.toString)
  }

  implicit val enumerable: Enumerable[ScheduleOfWorkingHours] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ScheduleOfWorkingHoursWrites extends Writes[ScheduleOfWorkingHours] {
    def writes(scheduleOfWorkingHours: ScheduleOfWorkingHours) = Json.toJson(scheduleOfWorkingHours.toString)
  }

  implicit object ScheduleOfWorkingHoursReads extends Reads[ScheduleOfWorkingHours] {
    override def reads(json: JsValue): JsResult[ScheduleOfWorkingHours] = json match {
      case JsString(Scheduledecidedforworker.toString) => JsSuccess(Scheduledecidedforworker)
      case JsString(Workerdecideschedule.toString) => JsSuccess(Workerdecideschedule)
      case JsString(WorkerAgreeSchedule.toString) => JsSuccess(WorkerAgreeSchedule)
      case JsString(Noschedulerequiredonlydeadlines.toString) => JsSuccess(Noschedulerequiredonlydeadlines)
      case _                          => JsError("Unknown scheduleOfWorkingHours")
    }
  }
}
