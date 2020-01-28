/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models.sections.setup

import models.{Enumerable, WithName}
import play.api.libs.json._
import viewmodels.{Radio, RadioOption}

sealed trait WhoAreYou

object WhoAreYou {

  case object Worker extends WithName("personDoingWork") with WhoAreYou
  case object Hirer extends WithName("endClient") with WhoAreYou
  case object Agency extends WithName("placingAgency") with WhoAreYou

  def values(showAgency: Boolean = true): Seq[WhoAreYou] = Seq(Worker, Hirer) ++ (if(showAgency) Seq(Agency) else Seq())

  def options(showAgency: Boolean = true): Seq[RadioOption] = values(showAgency).map { value => RadioOption("whoAreYou", value.toString, Radio) }

  implicit val enumerable: Enumerable[WhoAreYou] = Enumerable(values().map(v => v.toString -> v): _*)

  implicit val writes: Writes[WhoAreYou] = Writes { model => Json.toJson(model.toString) }

  implicit val reads: Reads[WhoAreYou] = Reads {
    case JsString(Worker.toString) => JsSuccess(Worker)
    case JsString(Hirer.toString)  => JsSuccess(Hirer)
    case JsString(Agency.toString) => JsSuccess(Agency)
    case _                         => JsError("Unknown aboutYouAnswer")
  }
}
