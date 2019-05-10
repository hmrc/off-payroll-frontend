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

sealed trait MoveWorker

object MoveWorker {

  case object CanMoveWorkerWithPermission extends WithName("canMoveWorkerWithPermission") with MoveWorker
  case object CanMoveWorkerWithoutPermission extends WithName("canMoveWorkerWithoutPermission") with MoveWorker
  case object CannotMoveWorkerWithoutNewAgreement extends WithName("cannotMoveWorkerWithoutNewAgreement") with MoveWorker

  val values: Seq[MoveWorker] = Seq(
    CanMoveWorkerWithPermission, CanMoveWorkerWithoutPermission, CannotMoveWorkerWithoutNewAgreement
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("moveWorker", value.toString, radio)
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
