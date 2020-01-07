/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.sections.control

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait ScheduleOfWorkingHours

object ScheduleOfWorkingHours {

  case object ScheduleDecidedForWorker extends WithName("scheduleDecidedForWorker") with ScheduleOfWorkingHours
  case object WorkerDecideSchedule extends WithName("workerDecideSchedule") with ScheduleOfWorkingHours
  case object WorkerAgreeSchedule extends WithName("workerAgreeSchedule") with ScheduleOfWorkingHours
  case object NoScheduleRequiredOnlyDeadlines extends WithName("noScheduleRequiredOnlyDeadlines") with ScheduleOfWorkingHours

  val values: Seq[ScheduleOfWorkingHours] = Seq(
    ScheduleDecidedForWorker, WorkerDecideSchedule, WorkerAgreeSchedule, NoScheduleRequiredOnlyDeadlines
  )

  def options: Seq[RadioOption] = values.map {
    value =>
        RadioOption("scheduleOfWorkingHours", value.toString, Radio, hasTailoredMsgs = true)
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
