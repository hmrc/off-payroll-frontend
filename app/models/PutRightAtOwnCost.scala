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

sealed trait PutRightAtOwnCost

object PutRightAtOwnCost {

  case object OutsideOfHoursNoCharge extends WithName("outsideOfHoursNoCharge") with PutRightAtOwnCost
  case object OutsideOfHoursNoCosts extends WithName("outsideOfHoursNoCosts") with PutRightAtOwnCost
  case object AsPartOfUsualRateInWorkingHours extends WithName("asPartOfUsualRateInWorkingHours") with PutRightAtOwnCost
  case object CannotBeCorrected extends WithName("cannotBeCorrected") with PutRightAtOwnCost
  case object NoObligationToCorrect extends WithName("noObligationToCorrect") with PutRightAtOwnCost

  val values: Seq[PutRightAtOwnCost] = Seq(
    OutsideOfHoursNoCharge, OutsideOfHoursNoCosts, AsPartOfUsualRateInWorkingHours, CannotBeCorrected, NoObligationToCorrect
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("putRightAtOwnCost", value.toString)
  }

  implicit val enumerable: Enumerable[PutRightAtOwnCost] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object PutRightAtOwnCostWrites extends Writes[PutRightAtOwnCost] {
    def writes(putRightAtOwnCost: PutRightAtOwnCost) = Json.toJson(putRightAtOwnCost.toString)
  }

  implicit object PutRightAtOwnCostReads extends Reads[PutRightAtOwnCost] {
    override def reads(json: JsValue): JsResult[PutRightAtOwnCost] = json match {
      case JsString(OutsideOfHoursNoCharge.toString) => JsSuccess(OutsideOfHoursNoCharge)
      case JsString(OutsideOfHoursNoCosts.toString) => JsSuccess(OutsideOfHoursNoCosts)
      case JsString(AsPartOfUsualRateInWorkingHours.toString) => JsSuccess(AsPartOfUsualRateInWorkingHours)
      case JsString(CannotBeCorrected.toString) => JsSuccess(CannotBeCorrected)
      case JsString(NoObligationToCorrect.toString) => JsSuccess(NoObligationToCorrect)
      case _                          => JsError("Unknown putRightAtOwnCost")
    }
  }
}
