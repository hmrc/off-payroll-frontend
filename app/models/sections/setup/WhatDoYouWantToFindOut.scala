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

sealed trait WhatDoYouWantToFindOut

object WhatDoYouWantToFindOut {

  case object IR35 extends WithName("ir35") with WhatDoYouWantToFindOut
  case object PAYE extends WithName("paye") with WhatDoYouWantToFindOut

  val values: Seq[WhatDoYouWantToFindOut] = Seq(IR35, PAYE)

  val options: Seq[RadioOption] = Seq(
    RadioOption("whatDoYouWantToFindOut", IR35.toString, Radio),
    RadioOption("whatDoYouWantToFindOut", PAYE.toString, Radio)
  )

  implicit val enumerable: Enumerable[WhatDoYouWantToFindOut] = Enumerable(values.map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhatDoYouWantToFindOut] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhatDoYouWantToFindOut] = Reads {
    case JsString(PAYE.toString) => JsSuccess(PAYE)
    case JsString(IR35.toString) => JsSuccess(IR35)
    case _                       => JsError("Unknown WhatDoYouWantToFindOut")
  }
}
