/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models.sections.financialRisk

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait PutRightAtOwnCost

object PutRightAtOwnCost extends FeatureSwitching {

  case object OutsideOfHoursNoCharge extends WithName("outsideOfHoursNoCharge") with PutRightAtOwnCost
  case object OutsideOfHoursNoCosts extends WithName("outsideOfHoursNoCosts") with PutRightAtOwnCost
  case object AsPartOfUsualRateInWorkingHours extends WithName("asPartOfUsualRateInWorkingHours") with PutRightAtOwnCost
  case object CannotBeCorrected extends WithName("cannotBeCorrected") with PutRightAtOwnCost
  case object NoObligationToCorrect extends WithName("noObligationToCorrect") with PutRightAtOwnCost

  val values: Seq[PutRightAtOwnCost] = Seq(
    OutsideOfHoursNoCharge, OutsideOfHoursNoCosts, AsPartOfUsualRateInWorkingHours, CannotBeCorrected, NoObligationToCorrect
  )

  def options(implicit frontendAppConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value => RadioOption(
      keyPrefix = "putRightAtOwnCost",
      option = value.toString,
      optionType = Radio,
      hasTailoredMsgs = true,
      dividerPrefix = false
    )
  }
  implicit val enumerable: Enumerable[PutRightAtOwnCost] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object PutRightAtOwnCostWrites extends Writes[PutRightAtOwnCost] {
    def writes(putRightAtOwnCost: PutRightAtOwnCost) = Json.toJson(putRightAtOwnCost.toString)
  }

  implicit object PutRightAtOwnCostReads extends Reads[PutRightAtOwnCost] {
    override def reads(json: JsValue): JsResult[PutRightAtOwnCost] = json match {
      case JsString(OutsideOfHoursNoCharge.toString) => JsSuccess(OutsideOfHoursNoCharge)
      case JsString(OutsideOfHoursNoCosts.toString) => JsSuccess(OutsideOfHoursNoCosts)
      case JsString(AsPartOfUsualRateInWorkingHours.toString) => JsSuccess(AsPartOfUsualRateInWorkingHours)
      case JsString(CannotBeCorrected.toString) => JsSuccess(CannotBeCorrected)
      case JsString(NoObligationToCorrect.toString) => JsSuccess(NoObligationToCorrect)
      case _                          => JsError("Unknown putRightAtOwnCost")
    }
  }
}
