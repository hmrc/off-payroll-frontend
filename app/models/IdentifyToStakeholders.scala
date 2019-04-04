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

sealed trait IdentifyToStakeholders

object IdentifyToStakeholders {

  case object WorkForEndClient extends WithName("workForEndClient") with IdentifyToStakeholders
  case object WorkAsIndependent extends WithName("workAsIndependent") with IdentifyToStakeholders
  case object WorkAsBusiness extends WithName("workAsBusiness") with IdentifyToStakeholders

  val values: Seq[IdentifyToStakeholders] = Seq(
    WorkForEndClient, WorkAsIndependent, WorkAsBusiness
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("identifyToStakeholders", value.toString)
  }

  implicit val enumerable: Enumerable[IdentifyToStakeholders] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object IdentifyToStakeholdersWrites extends Writes[IdentifyToStakeholders] {
    def writes(identifyToStakeholders: IdentifyToStakeholders) = Json.toJson(identifyToStakeholders.toString)
  }

  implicit object IdentifyToStakeholdersReads extends Reads[IdentifyToStakeholders] {
    override def reads(json: JsValue): JsResult[IdentifyToStakeholders] = json match {
      case JsString(WorkForEndClient.toString) => JsSuccess(WorkForEndClient)
      case JsString(WorkAsIndependent.toString) => JsSuccess(WorkAsIndependent)
      case JsString(WorkAsBusiness.toString) => JsSuccess(WorkAsBusiness)
      case _                          => JsError("Unknown identifyToStakeholders")
    }
  }
}
