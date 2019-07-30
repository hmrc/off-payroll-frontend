package models

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import play.api.libs.json.{JsError, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import viewmodels.{Radio, RadioOption}

sealed trait ExclusiveContract

object ExclusiveContract extends FeatureSwitching {

  case object UnableToProvideServices extends WithName("unableToProvideServices") with ExclusiveContract
  case object AbleToProvideServicesWithPermission extends WithName("ableToProvideServicesWithPermission") with ExclusiveContract
  case object AbleToProvideServices extends WithName("ableToProvideServices") with ExclusiveContract

  val values: Seq[ExclusiveContract] = Seq(
    UnableToProvideServices, AbleToProvideServicesWithPermission, AbleToProvideServices
  )

  def options(implicit frontendAppConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value => RadioOption(
      keyPrefix = "exclusiveContract",
      option = value.toString,
      optionType = Radio,
      hasTailoredMsgs = true,
      dividerPrefix = false,
      hasOptimisedMsgs = isEnabled(OptimisedFlow)
    )
  }

  implicit val enumerable: Enumerable[ExclusiveContract] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ExclusiveContractWrites extends Writes[ExclusiveContract] {
    def writes(exclusiveContract: ExclusiveContract): JsValue = Json.toJson(exclusiveContract.toString)
  }

  implicit object ExclusiveContractReads extends Reads[ExclusiveContract] {
    override def reads(json: JsValue): JsResult[ExclusiveContract] = json match {
      case JsString(UnableToProvideServices.toString) => JsSuccess(UnableToProvideServices)
      case JsString(AbleToProvideServicesWithPermission.toString) => JsSuccess(AbleToProvideServicesWithPermission)
      case JsString(AbleToProvideServices.toString) => JsSuccess(AbleToProvideServices)
      case _                          => JsError("Unknown exclusiveContract")
    }
  }
}

