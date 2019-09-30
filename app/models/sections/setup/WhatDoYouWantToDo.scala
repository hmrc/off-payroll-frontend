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

package models.sections.setup

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait WhatDoYouWantToDo

object WhatDoYouWantToDo {

  case object MakeNewDetermination extends WithName("makeNewDetermination") with WhatDoYouWantToDo
  case object CheckDetermination extends WithName("checkDetermination") with WhatDoYouWantToDo


  val values: Seq[WhatDoYouWantToDo] = Seq(MakeNewDetermination, CheckDetermination)

  val options: Seq[RadioOption] = Seq(
    RadioOption("whatDoYouWantToDo", MakeNewDetermination.toString, Radio),
    RadioOption("whatDoYouWantToDo", CheckDetermination.toString, Radio)
  )

  implicit val enumerable: Enumerable[WhatDoYouWantToDo] = Enumerable(values.map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhatDoYouWantToDo] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhatDoYouWantToDo] = Reads {
    case JsString(MakeNewDetermination.toString) => JsSuccess(MakeNewDetermination)
    case JsString(CheckDetermination.toString) => JsSuccess(CheckDetermination)
    case _                         => JsError("Unknown whatDoYouWantToDo")
  }
}
