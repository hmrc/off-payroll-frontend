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

import play.api.libs.json.{JsError, JsString, JsSuccess, Json, Reads, Writes}
import viewmodels.{RadioOption, radio}

sealed trait WhichDescribesYouAnswer

object WhichDescribesYouAnswer {

  case object Worker1 extends WithName("worker.one") with WhichDescribesYouAnswer
  case object Worker2 extends WithName("worker.two") with WhichDescribesYouAnswer
  case object Client1 extends WithName("client.one") with WhichDescribesYouAnswer
  case object Client2 extends WithName("client.two") with WhichDescribesYouAnswer
  case object Agency extends WithName("agency") with WhichDescribesYouAnswer

  val values: Seq[WhichDescribesYouAnswer] = Seq(Worker1, Client1, Worker2, Client2, Agency)

  val options: Seq[RadioOption] = Seq(
    RadioOption("whichDescribesYou", Worker1.toString, radio),
    RadioOption("whichDescribesYou", Worker2.toString, radio),
    RadioOption("whichDescribesYou", Client1.toString, radio, hasTailoredMsgs = false, dividerPrefix = true),
    RadioOption("whichDescribesYou", Client2.toString, radio),
    RadioOption("whichDescribesYou", Agency.toString, radio)
  )

  implicit val enumerable: Enumerable[WhichDescribesYouAnswer] = Enumerable(values.map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhichDescribesYouAnswer] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhichDescribesYouAnswer] = Reads {
    case JsString(Worker1.toString) => JsSuccess(Worker1)
    case JsString(Client1.toString) => JsSuccess(Client1)
    case JsString(Worker2.toString) => JsSuccess(Worker2)
    case JsString(Client2.toString) => JsSuccess(Client2)
    case JsString(Agency.toString) => JsSuccess(Agency)
    case _                         => JsError("Unknown whichDescribesYouAnswer")
  }
}