/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.control

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait MoveWorker

object MoveWorker {

  case object CanMoveWorkerWithPermission extends WithName("canMoveWorkerWithPermission") with MoveWorker
  case object CanMoveWorkerWithoutPermission extends WithName("canMoveWorkerWithoutPermission") with MoveWorker
  case object CannotMoveWorkerWithoutNewAgreement extends WithName("cannotMoveWorkerWithoutNewAgreement") with MoveWorker

  def values: Seq[MoveWorker] =
     Seq(CanMoveWorkerWithoutPermission, CanMoveWorkerWithPermission, CannotMoveWorkerWithoutNewAgreement)

  def options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("moveWorker", value.toString, Radio, hasTailoredMsgs = true)
  }

  implicit val enumerable: Enumerable[MoveWorker] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object MoveWorkerWrites extends Writes[MoveWorker] {
    def writes(moveWorker: MoveWorker) = Json.toJson(moveWorker.toString)
  }

  implicit object MoveWorkerReads extends Reads[MoveWorker] {
    override def reads(json: JsValue): JsResult[MoveWorker] = json match {
      case JsString(CanMoveWorkerWithPermission.toString) => JsSuccess(CanMoveWorkerWithPermission)
      case JsString(CanMoveWorkerWithoutPermission.toString) => JsSuccess(CanMoveWorkerWithoutPermission)
      case JsString(CannotMoveWorkerWithoutNewAgreement.toString) => JsSuccess(CannotMoveWorkerWithoutNewAgreement)
      case _                          => JsError("Unknown moveWorker")
    }
  }
}
