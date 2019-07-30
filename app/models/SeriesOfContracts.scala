package models

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import play.api.libs.json.{JsError, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import viewmodels.{Radio, RadioOption}

sealed trait SeriesOfContracts

object SeriesOfContracts extends FeatureSwitching {

  case object ContractIsPartOfASeries extends WithName("contractIsPartOfASeries") with SeriesOfContracts
  case object StandAloneContract extends WithName("standAloneContract") with SeriesOfContracts
  case object ContractCouldBeExtended extends WithName("contractCouldBeExtended") with SeriesOfContracts

  val values: Seq[SeriesOfContracts] = Seq(
    ContractIsPartOfASeries, StandAloneContract, ContractCouldBeExtended
  )

  def options(implicit frontendAppConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value => RadioOption(
      keyPrefix = "seriesOfContracts",
      option = value.toString,
      optionType = Radio,
      hasTailoredMsgs = true,
      dividerPrefix = false,
      hasOptimisedMsgs = isEnabled(OptimisedFlow)
    )
  }

  implicit val enumerable: Enumerable[SeriesOfContracts] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object SeriesOfContractsWrites extends Writes[SeriesOfContracts] {
    def writes(seriesOfContracts: SeriesOfContracts): JsValue = Json.toJson(seriesOfContracts.toString)
  }

  implicit object SeriesOfContractsReads extends Reads[SeriesOfContracts] {
    override def reads(json: JsValue): JsResult[SeriesOfContracts] = json match {
      case JsString(ContractIsPartOfASeries.toString) => JsSuccess(ContractIsPartOfASeries)
      case JsString(StandAloneContract.toString) => JsSuccess(StandAloneContract)
      case JsString(ContractCouldBeExtended.toString) => JsSuccess(ContractCouldBeExtended)
      case _                          => JsError("Unknown seriesOfContracts")
    }
  }
}
