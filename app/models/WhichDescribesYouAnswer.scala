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
import viewmodels.{RadioOption, Radio}

sealed trait WhichDescribesYouAnswer

object WhichDescribesYouAnswer {

  case object WorkerPAYE extends WithName("worker.paye") with WhichDescribesYouAnswer
  case object WorkerIR35 extends WithName("worker.ir35") with WhichDescribesYouAnswer
  case object ClientPAYE extends WithName("client.paye") with WhichDescribesYouAnswer
  case object ClientIR35 extends WithName("client.ir35") with WhichDescribesYouAnswer
  case object Agency extends WithName("agency") with WhichDescribesYouAnswer

  val values: Seq[WhichDescribesYouAnswer] = Seq(WorkerPAYE, ClientPAYE, WorkerIR35, ClientIR35, Agency)

  val options: Seq[RadioOption] = Seq(
    RadioOption("whichDescribesYou", WorkerIR35.toString, Radio),
    RadioOption("whichDescribesYou", ClientIR35.toString, Radio),
    RadioOption("whichDescribesYou", Agency.toString, Radio),
    RadioOption("whichDescribesYou", WorkerPAYE.toString, Radio),
    RadioOption("whichDescribesYou", ClientPAYE.toString, Radio)
  )

  implicit val enumerable: Enumerable[WhichDescribesYouAnswer] = Enumerable(values.map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhichDescribesYouAnswer] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhichDescribesYouAnswer] = Reads {
    case JsString(WorkerPAYE.toString) => JsSuccess(WorkerPAYE)
    case JsString(ClientPAYE.toString) => JsSuccess(ClientPAYE)
    case JsString(WorkerIR35.toString) => JsSuccess(WorkerIR35)
    case JsString(ClientIR35.toString) => JsSuccess(ClientIR35)
    case JsString(Agency.toString) => JsSuccess(Agency)
    case _                         => JsError("Unknown whichDescribesYouAnswer")
  }
}
