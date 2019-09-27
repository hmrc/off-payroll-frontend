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

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait SignificantWorkingTime

object SignificantWorkingTime extends FeatureSwitching {

  case object ConsumesSignificantAmount extends WithName("consumesSignificantAmount") with SignificantWorkingTime
  case object NoSignificantAmount extends WithName("noSignificantAmount") with SignificantWorkingTime
  case object TimeButNotMoney extends WithName("timeButNotMoney") with SignificantWorkingTime
  case object MoneyButNotTime extends WithName("moneyButNotTime") with SignificantWorkingTime

  val values: Seq[SignificantWorkingTime] = Seq(
    ConsumesSignificantAmount, NoSignificantAmount, TimeButNotMoney, MoneyButNotTime
  )

  implicit val enumerable: Enumerable[SignificantWorkingTime] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object SignificantWorkingTimeWrites extends Writes[SignificantWorkingTime] {
    def writes(significantWorkingTime: SignificantWorkingTime): JsValue = Json.toJson(significantWorkingTime.toString)
  }
}
