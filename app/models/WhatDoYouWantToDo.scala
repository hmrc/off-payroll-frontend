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

sealed trait WhatDoYouWantToDo

object WhatDoYouWantToDo {

  case object makeNewDetermination extends WithName("makeNewDetermination") with WhatDoYouWantToDo
  case object checkDetermination extends WithName("checkDetermination") with WhatDoYouWantToDo


  val values: Seq[WhatDoYouWantToDo] = Seq(makeNewDetermination, checkDetermination)

  val options: Seq[RadioOption] = Seq(
    RadioOption("whatDoYouWantToDo", makeNewDetermination.toString, Radio),
    RadioOption("whatDoYouWantToDo", checkDetermination.toString, Radio)
  )

  implicit val enumerable: Enumerable[WhatDoYouWantToDo] = Enumerable(values.map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhatDoYouWantToDo] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhatDoYouWantToDo] = Reads {
    case JsString(makeNewDetermination.toString) => JsSuccess(makeNewDetermination)
    case JsString(checkDetermination.toString) => JsSuccess(checkDetermination)
    case _                         => JsError("Unknown whatDoYouWantToDo")
  }
}
