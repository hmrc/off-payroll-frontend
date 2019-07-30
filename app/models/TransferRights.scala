package models

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import play.api.libs.json.{JsError, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import viewmodels.{Radio, RadioOption}

sealed trait TransferRights

object TransferRights extends FeatureSwitching {

  case object RightsTransferredToClient extends WithName("rightsTransferredToClient") with TransferRights
  case object AbleToTransferRights extends WithName("ableToTransferRights") with TransferRights
  case object RetainOwnershipRights extends WithName("retainOwnershipRights") with TransferRights
  case object NoRightsArise extends WithName("noRightsArise") with TransferRights

  val values: Seq[TransferRights] = Seq(
    RightsTransferredToClient, AbleToTransferRights, RetainOwnershipRights, NoRightsArise
  )

  def options(implicit frontendAppConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value => RadioOption(
      keyPrefix = "transferRights",
      option = value.toString,
      optionType = Radio,
      hasTailoredMsgs = true,
      dividerPrefix = false,
      hasOptimisedMsgs = isEnabled(OptimisedFlow)
    )
  }

  implicit val enumerable: Enumerable[TransferRights] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object TransferWrites extends Writes[TransferRights] {
    def writes(transferRights: TransferRights): JsValue = Json.toJson(transferRights.toString)
  }

  implicit object TransferRightsReads extends Reads[TransferRights] {
    override def reads(json: JsValue): JsResult[TransferRights] = json match {
      case JsString(RightsTransferredToClient.toString) => JsSuccess(RightsTransferredToClient)
      case JsString(AbleToTransferRights.toString) => JsSuccess(AbleToTransferRights)
      case JsString(RetainOwnershipRights.toString) => JsSuccess(RetainOwnershipRights)
      case JsString(NoRightsArise.toString) => JsSuccess(NoRightsArise)
      case _                          => JsError("Unknown transferRights")
    }
  }
}

