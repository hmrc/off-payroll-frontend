/*
 * Copyright 2022 HM Revenue & Customs
 *
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
