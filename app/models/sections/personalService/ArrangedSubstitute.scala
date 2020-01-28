/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.sections.personalService

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait ArrangedSubstitute

object ArrangedSubstitute extends FeatureSwitching {

  case object YesClientAgreed extends WithName("yesClientAgreed") with ArrangedSubstitute
  case object YesClientNotAgreed extends WithName("notAgreedWithClient") with ArrangedSubstitute
  case object No extends WithName("noSubstitutionHappened") with ArrangedSubstitute

  val values: Seq[ArrangedSubstitute] = Seq(
    YesClientAgreed, YesClientNotAgreed, No
  )

  def options(implicit appConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value =>
      RadioOption(
        "arrangedSubstitute",
        value.toString,
        Radio,
        hasTailoredMsgs = true,
        dividerPrefix = false
      )
  }

  implicit val enumerable: Enumerable[ArrangedSubstitute] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ArrangedSubstituteWrites extends Writes[ArrangedSubstitute] {
    def writes(arrangedSubstitute: ArrangedSubstitute) = Json.toJson(arrangedSubstitute.toString)
  }

  implicit object ArrangedSubstituteReads extends Reads[ArrangedSubstitute] {
    override def reads(json: JsValue): JsResult[ArrangedSubstitute] = json match {
      case JsString(YesClientAgreed.toString) => JsSuccess(YesClientAgreed)
      case JsString(YesClientNotAgreed.toString) => JsSuccess(YesClientNotAgreed)
      case JsString(No.toString) => JsSuccess(No)
      case _ => JsError("Unknown arrangedSubstitute")
    }
  }
}
