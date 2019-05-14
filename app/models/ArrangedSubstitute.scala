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

import play.api.libs.json._
import viewmodels.{RadioOption, radio}

sealed trait ArrangedSubstitute

object ArrangedSubstitute {

  case object YesClientAgreed extends WithName("yesClientAgreed") with ArrangedSubstitute
  case object YesClientNotAgreed extends WithName("yesClientNotAgreed") with ArrangedSubstitute
  case object No extends WithName("no") with ArrangedSubstitute

  val values: Seq[ArrangedSubstitute] = Seq(
    YesClientAgreed, YesClientNotAgreed, No
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("arrangedSubstitute", value.toString, radio, hasTailoredMsgs = true)
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
      case _                          => JsError("Unknown arrangedSubstitute")
    }
  }
}
