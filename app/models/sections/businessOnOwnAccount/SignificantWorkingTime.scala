/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait SignificantWorkingTime

object SignificantWorkingTime extends FeatureSwitching {

  case object ConsumesSignificantAmount extends WithName("consumesSignificantAmount") with SignificantWorkingTime
  case object NoSignificantAmount extends WithName("noSignificantAmount") with SignificantWorkingTime

  val values: Seq[SignificantWorkingTime] = Seq(
    ConsumesSignificantAmount, NoSignificantAmount
  )

  implicit val enumerable: Enumerable[SignificantWorkingTime] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object SignificantWorkingTimeWrites extends Writes[SignificantWorkingTime] {
    def writes(significantWorkingTime: SignificantWorkingTime): JsValue = Json.toJson(significantWorkingTime.toString)
  }
}
