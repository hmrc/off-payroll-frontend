/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models.sections.partAndParcel

import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, OptimisedFlow}
import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait IdentifyToStakeholders

object IdentifyToStakeholders extends FeatureSwitching {

  case object WorkForEndClient extends WithName("workForEndClient") with IdentifyToStakeholders
  case object WorkAsIndependent extends WithName("workAsIndependent") with IdentifyToStakeholders
  case object WorkAsBusiness extends WithName("workAsBusiness") with IdentifyToStakeholders
  case object WouldNotHappen extends WithName("wouldNotHappen") with IdentifyToStakeholders

  def values(optimised: Boolean = false): Seq[IdentifyToStakeholders] =
    if (optimised) {
      Seq(WorkForEndClient, WorkAsIndependent, WorkAsBusiness, WouldNotHappen)
    } else {
      Seq(WorkForEndClient, WorkAsIndependent, WorkAsBusiness)
    }

  def options(implicit appConfig: FrontendAppConfig): Seq[RadioOption] = values(isEnabled(OptimisedFlow)).map { value =>
    RadioOption(
      keyPrefix = "identifyToStakeholders",
      option = value.toString,
      optionType = Radio,
      dividerPrefix = false,
      hasTailoredMsgs = true,
      hasOptimisedMsgs = isEnabled(OptimisedFlow)
    )
  }

  implicit val enumerable: Enumerable[IdentifyToStakeholders] =
    Enumerable(values(true).map(v => v.toString -> v): _*)

  implicit object IdentifyToStakeholdersWrites extends Writes[IdentifyToStakeholders] {
    def writes(identifyToStakeholders: IdentifyToStakeholders) = Json.toJson(identifyToStakeholders.toString)
  }

  implicit object IdentifyToStakeholdersReads extends Reads[IdentifyToStakeholders] {
    override def reads(json: JsValue): JsResult[IdentifyToStakeholders] = json match {
      case JsString(WorkForEndClient.toString) => JsSuccess(WorkForEndClient)
      case JsString(WorkAsIndependent.toString) => JsSuccess(WorkAsIndependent)
      case JsString(WorkAsBusiness.toString) => JsSuccess(WorkAsBusiness)
      case JsString(WouldNotHappen.toString) => JsSuccess(WouldNotHappen)
      case _                          => JsError("Unknown identifyToStakeholders")
    }
  }
}
