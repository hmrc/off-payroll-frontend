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
import play.api.libs.json._
import viewmodels.{RadioOption, Radio}

sealed trait RightsOfWork

object RightsOfWork extends FeatureSwitching {

  case object Yes extends WithName("yes") with RightsOfWork
  case object No extends WithName("no") with RightsOfWork
  case object NotApplicable extends WithName("notapplicable") with RightsOfWork

  val values: Seq[RightsOfWork] = Seq(
    Yes, No, NotApplicable
  )

  def options(implicit appConfig: FrontendAppConfig): Seq[RadioOption] = values.map {
    value =>
      RadioOption(
        "RightsOfWork",
        value.toString,
        Radio,
        hasTailoredMsgs = true,
        hasOptimisedMsgs = isEnabled(OptimisedFlow),
        dividerPrefix = false
      )
  }

  implicit val enumerable: Enumerable[RightsOfWork] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object RightsOfWorkWrites extends Writes[RightsOfWork] {
    def writes(RightsOfWork: RightsOfWork) = Json.toJson(RightsOfWork.toString)
  }

  implicit object RightsOfWorkReads extends Reads[RightsOfWork] {
    override def reads(json: JsValue): JsResult[RightsOfWork] = json match {
      case JsString(Yes.toString) => JsSuccess(Yes)
      case JsString(No.toString) => JsSuccess(No)
      case JsString(NotApplicable.toString) => JsSuccess(NotApplicable)
      case _                          => JsError("Unknown RightsOfWork")
    }
  }
}