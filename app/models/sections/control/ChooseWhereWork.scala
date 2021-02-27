/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.control

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait ChooseWhereWork

object ChooseWhereWork {

  case object WorkerCannotChoose extends WithName("workerCannotChoose") with ChooseWhereWork
  case object WorkerChooses extends WithName("workerChooses") with ChooseWhereWork
  case object NoLocationRequired extends WithName("noLocationRequired") with ChooseWhereWork
  case object WorkerAgreeWithOthers extends WithName("workerAgreeWithOthers") with ChooseWhereWork

  def values: Seq[ChooseWhereWork] =
    Seq(WorkerCannotChoose, WorkerChooses, NoLocationRequired, WorkerAgreeWithOthers)

  def options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("chooseWhereWork", value.toString, Radio, hasTailoredMsgs = true)
  }

  implicit val enumerable: Enumerable[ChooseWhereWork] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ChooseWhereWorkWrites extends Writes[ChooseWhereWork] {
    def writes(chooseWhereWork: ChooseWhereWork) = Json.toJson(chooseWhereWork.toString)
  }

  implicit object ChooseWhereWorkReads extends Reads[ChooseWhereWork] {
    override def reads(json: JsValue): JsResult[ChooseWhereWork] = json match {
      case JsString(WorkerChooses.toString) => JsSuccess(WorkerChooses)
      case JsString(WorkerCannotChoose.toString) => JsSuccess(WorkerCannotChoose)
      case JsString(NoLocationRequired.toString) => JsSuccess(NoLocationRequired)
      case JsString(WorkerAgreeWithOthers.toString) => JsSuccess(WorkerAgreeWithOthers)
      case _                          => JsError("Unknown chooseWhereWork")
    }
  }
}
