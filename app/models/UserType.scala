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

import models.sections.setup.WhoAreYou
import play.api.libs.json._

trait UserType

object UserType {

  def apply(whoAreYou: WhoAreYou): UserType = whoAreYou match {
    case WhoAreYou.Worker => Worker
    case WhoAreYou.Client => Hirer
    case WhoAreYou.Agency => Agency
  }

  val values = Seq(Worker, Hirer, Agency)

  case object Worker extends WithName("worker") with UserType
  case object Agency extends WithName("agency") with UserType
  case object Hirer extends WithName("hirer") with UserType

  implicit val writes: Writes[UserType] = Writes {
    case Worker => JsString(WhoAreYou.Worker.toString)
    case Hirer => JsString(WhoAreYou.Client.toString)
    case Agency => JsString(WhoAreYou.Agency.toString)
  }

  implicit val reads: Reads[UserType] = Reads {
    case JsString(WhoAreYou.Worker.toString) => JsSuccess(Worker)
    case JsString(WhoAreYou.Client.toString) => JsSuccess(Hirer)
    case JsString(WhoAreYou.Agency.toString) => JsSuccess(Agency)
    case _ => JsError("Unknown UserType")
  }
}
