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

import config.featureSwitch.FeatureSwitching
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait HowWorkIsDone

object HowWorkIsDone {

  case object NoWorkerInputAllowed extends WithName("noWorkerInputAllowed") with HowWorkIsDone
  case object WorkerDecidesWithoutInput extends WithName("workerDecidesWithoutInput") with HowWorkIsDone
  case object WorkerFollowStrictEmployeeProcedures extends WithName("workerFollowStrictEmployeeProcedures") with HowWorkIsDone
  case object WorkerAgreeWithOthers extends WithName("workerAgreeWithOthers") with HowWorkIsDone

  def values(optimised: Boolean = false): Seq[HowWorkIsDone] =
    if(optimised) {
      Seq(NoWorkerInputAllowed, WorkerDecidesWithoutInput, WorkerAgreeWithOthers, WorkerFollowStrictEmployeeProcedures)
    }else{
      Seq(NoWorkerInputAllowed, WorkerDecidesWithoutInput, WorkerFollowStrictEmployeeProcedures, WorkerAgreeWithOthers)

    }

  def options(optimised: Boolean = false): Seq[RadioOption] = values(optimised).map {
    value =>

      if(optimised){
        RadioOption("optimised.howWorkIsDone", value.toString, Radio, hasTailoredMsgs = true)
      } else {
        RadioOption("howWorkIsDone", value.toString, Radio, hasTailoredMsgs = true)
      }
  }

  implicit val enumerable: Enumerable[HowWorkIsDone] =
    Enumerable(values().map(v => v.toString -> v): _*)

  implicit object HowWorkIsDoneWrites extends Writes[HowWorkIsDone] {
    def writes(howWorkIsDone: HowWorkIsDone) = Json.toJson(howWorkIsDone.toString)
  }

  implicit object HowWorkIsDoneReads extends Reads[HowWorkIsDone] {
    override def reads(json: JsValue): JsResult[HowWorkIsDone] = json match {
      case JsString(NoWorkerInputAllowed.toString) => JsSuccess(NoWorkerInputAllowed)
      case JsString(WorkerDecidesWithoutInput.toString) => JsSuccess(WorkerDecidesWithoutInput)
      case JsString(WorkerFollowStrictEmployeeProcedures.toString) => JsSuccess(WorkerFollowStrictEmployeeProcedures)
      case JsString(WorkerAgreeWithOthers.toString) => JsSuccess(WorkerAgreeWithOthers)
      case _                          => JsError("Unknown howWorkIsDone")
    }
  }
}
