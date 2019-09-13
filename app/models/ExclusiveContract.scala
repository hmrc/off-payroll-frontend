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

import config.featureSwitch.FeatureSwitching
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait ExclusiveContract

object ExclusiveContract extends FeatureSwitching {

  case object UnableToProvideServices extends WithName("unableToProvideServices") with ExclusiveContract
  case object AbleToProvideServicesWithPermission extends WithName("ableToProvideServicesWithPermission") with ExclusiveContract
  case object AbleToProvideServices extends WithName("ableToProvideServices") with ExclusiveContract

  val values: Seq[ExclusiveContract] = Seq(
    UnableToProvideServices, AbleToProvideServicesWithPermission, AbleToProvideServices
  )

  implicit val enumerable: Enumerable[ExclusiveContract] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ExclusiveContractWrites extends Writes[ExclusiveContract] {
    def writes(exclusiveContract: ExclusiveContract): JsValue = Json.toJson(exclusiveContract.toString)
  }
}

