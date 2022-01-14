/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
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

