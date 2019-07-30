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

