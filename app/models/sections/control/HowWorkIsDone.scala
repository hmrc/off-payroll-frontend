/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models.sections.control

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait HowWorkIsDone

object HowWorkIsDone {

  case object NoWorkerInputAllowed extends WithName("noWorkerInputAllowed") with HowWorkIsDone
  case object WorkerDecidesWithoutInput extends WithName("workerDecidesWithoutInput") with HowWorkIsDone
  case object WorkerFollowStrictEmployeeProcedures extends WithName("workerFollowStrictEmployeeProcedures") with HowWorkIsDone
  case object WorkerAgreeWithOthers extends WithName("workerAgreeWithOthers") with HowWorkIsDone

  def values: Seq[HowWorkIsDone] = Seq(NoWorkerInputAllowed, WorkerDecidesWithoutInput, WorkerAgreeWithOthers, WorkerFollowStrictEmployeeProcedures)

  def options: Seq[RadioOption] = values.map {
    value => RadioOption("howWorkIsDone", value.toString, Radio, hasTailoredMsgs = true)
  }

  implicit val enumerable: Enumerable[HowWorkIsDone] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object HowWorkIsDoneWrites extends Writes[HowWorkIsDone] {
    def writes(howWorkIsDone: HowWorkIsDone) = Json.toJson(howWorkIsDone.toString)
  }

  implicit object HowWorkIsDoneReads extends Reads[HowWorkIsDone] {
    override def reads(json: JsValue): JsResult[HowWorkIsDone] = json match {
      case JsString(NoWorkerInputAllowed.toString) => JsSuccess(NoWorkerInputAllowed)
      case JsString(WorkerDecidesWithoutInput.toString) => JsSuccess(WorkerDecidesWithoutInput)
      case JsString(WorkerFollowStrictEmployeeProcedures.toString) => JsSuccess(WorkerFollowStrictEmployeeProcedures)
      case JsString(WorkerAgreeWithOthers.toString) => JsSuccess(WorkerAgreeWithOthers)
      case _                          => JsError("Unknown howWorkIsDone")
    }
  }
}
