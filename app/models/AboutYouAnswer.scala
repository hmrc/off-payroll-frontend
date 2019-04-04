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

sealed trait AboutYouAnswer

object AboutYouAnswer {

  case object Worker extends WithName("worker") with AboutYouAnswer
  case object Client extends WithName("client") with AboutYouAnswer
  case object Agency extends WithName("agency") with AboutYouAnswer
  case object NoneOfAbove extends WithName("noneOfAbove") with AboutYouAnswer

  val values: Seq[AboutYouAnswer] = Seq(
    Worker, Client, Agency, NoneOfAbove
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("value", value.toString)
  }

  implicit val enumerable: Enumerable[AboutYouAnswer] =
    Enumerable(values.toSeq.map(v => v.toString -> v): _*)

  implicit object AboutYouAnswerWrites extends Writes[AboutYouAnswer] {
    def writes(aboutYouAnswer: AboutYouAnswer): JsValue = Json.toJson(aboutYouAnswer.toString)
  }

  implicit object AboutYouAnswerReads extends Reads[AboutYouAnswer] {
    override def reads(json: JsValue): JsResult[AboutYouAnswer] = json match {
      case JsString(Worker.toString) => JsSuccess(Worker)
      case JsString(Client.toString) => JsSuccess(Client)
      case JsString(Agency.toString) => JsSuccess(Agency)
      case JsString(NoneOfAbove.toString) => JsSuccess(NoneOfAbove)
      case _                          => JsError("Unknown aboutYouAnswer")
    }
  }
}
