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
import viewmodels.RadioOption

sealed trait ArrangedSubstitue

object ArrangedSubstitue {

  case object YesClientAgreed extends WithName("yesClientAgreed") with ArrangedSubstitue
  case object YesClientNotAgreed extends WithName("yesClientNotAgreed") with ArrangedSubstitue
  case object No extends WithName("no") with ArrangedSubstitue

  val values: Seq[ArrangedSubstitue] = Seq(
    YesClientAgreed, YesClientNotAgreed, No
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("arrangedSubstitue", value.toString)
  }

  implicit val enumerable: Enumerable[ArrangedSubstitue] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object ArrangedSubstitueWrites extends Writes[ArrangedSubstitue] {
    def writes(arrangedSubstitue: ArrangedSubstitue) = Json.toJson(arrangedSubstitue.toString)
  }

  implicit object ArrangedSubstitueReads extends Reads[ArrangedSubstitue] {
    override def reads(json: JsValue): JsResult[ArrangedSubstitue] = json match {
      case JsString(YesClientAgreed.toString) => JsSuccess(YesClientAgreed)
      case JsString(YesClientNotAgreed.toString) => JsSuccess(YesClientNotAgreed)
      case JsString(No.toString) => JsSuccess(No)
      case _                          => JsError("Unknown arrangedSubstitue")
    }
  }
}
