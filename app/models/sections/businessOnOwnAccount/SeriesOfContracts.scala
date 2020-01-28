/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
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
