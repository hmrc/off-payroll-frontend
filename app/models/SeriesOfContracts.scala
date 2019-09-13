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

sealed trait SeriesOfContracts

object SeriesOfContracts extends FeatureSwitching {

  case object ContractIsPartOfASeries extends WithName("contractIsPartOfASeries") with SeriesOfContracts
  case object StandAloneContract extends WithName("standAloneContract") with SeriesOfContracts
  case object ContractCouldBeExtended extends WithName("contractCouldBeExtended") with SeriesOfContracts

  val values: Seq[SeriesOfContracts] = Seq(
    ContractIsPartOfASeries, StandAloneContract, ContractCouldBeExtended
  )

  implicit val enumerable: Enumerable[SeriesOfContracts] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object SeriesOfContractsWrites extends Writes[SeriesOfContracts] {
    def writes(seriesOfContracts: SeriesOfContracts): JsValue = Json.toJson(seriesOfContracts.toString)
  }
}
