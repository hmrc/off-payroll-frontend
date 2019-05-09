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

sealed trait UserType

object UserType {

  def apply(aboutYouAnswer: AboutYouAnswer): UserType = aboutYouAnswer match {
    case AboutYouAnswer.Worker => Worker
    case AboutYouAnswer.Client => Hirer
    case AboutYouAnswer.Agency => Agency
  }

  val values = Seq(Worker, Hirer, Agency)

  case object Worker extends WithName("worker") with UserType
  case object Agency extends WithName("agency") with UserType
  case object Hirer extends WithName("hirer") with UserType

  implicit val writes: Writes[UserType] = Writes { user =>
    Json.toJson(user.toString)
  }

  implicit val reads: Reads[UserType] = Reads {
    case JsString(Worker.toString) => JsSuccess(Worker)
    case JsString(Hirer.toString) => JsSuccess(Hirer)
    case JsString(Agency.toString) => JsSuccess(Agency)
    case _ => JsError("Unknown UserType")
  }
}
