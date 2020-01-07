/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.sections.businessOnOwnAccount

import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json.{JsValue, Json, Writes}

sealed trait TransferRights

object TransferRights extends FeatureSwitching {

  case object RightsTransferredToClient extends WithName("rightsTransferredToClient") with TransferRights
  case object AbleToTransferRights extends WithName("ableToTransferRights") with TransferRights
  case object RetainOwnershipRights extends WithName("retainOwnershipRights") with TransferRights
  case object NoRightsArise extends WithName("noRightsArise") with TransferRights

  val values: Seq[TransferRights] = Seq(
    RightsTransferredToClient, AbleToTransferRights, RetainOwnershipRights, NoRightsArise
  )

  implicit val enumerable: Enumerable[TransferRights] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object TransferWrites extends Writes[TransferRights] {
    def writes(transferRights: TransferRights): JsValue = Json.toJson(transferRights.toString)
  }
}

