/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait MultipleEngagements

object MultipleEngagements extends FeatureSwitching {

  case object ProvidedServicesToOtherEngagers extends WithName("providedServicesToOtherEngagers") with MultipleEngagements
  case object OnlyContractForPeriod extends WithName("onlyContractForPeriod") with MultipleEngagements
  case object NoKnowledgeOfExternalActivity extends WithName("noKnowledgeOfExternalActivity") with MultipleEngagements

  val values: Seq[MultipleEngagements] = Seq(
    ProvidedServicesToOtherEngagers, OnlyContractForPeriod, NoKnowledgeOfExternalActivity
  )

  implicit val enumerable: Enumerable[MultipleEngagements] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object MultipleEngagementsWrites extends Writes[MultipleEngagements] {
    def writes(multipleEngagements: MultipleEngagements): JsValue = Json.toJson(multipleEngagements.toString)
  }
}
