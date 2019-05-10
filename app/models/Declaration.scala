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
import viewmodels.{RadioOption, checkbox}

sealed trait Declaration

object Declaration {

  case object Agree extends WithName("agree") with Declaration

  val values: Seq[Declaration] = Seq(Agree)

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("declaration", value.toString, checkbox)
  }

  implicit val enumerable: Enumerable[Declaration] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object DeclarationWrites extends Writes[Declaration] {
    def writes(cannotClaimAsExpense: Declaration) = Json.toJson(cannotClaimAsExpense.toString)
  }

  implicit object DeclarationReads extends Reads[Declaration] {
    override def reads(json: JsValue): JsResult[Declaration] = json match {
      case JsString(Agree.toString) => JsSuccess(Agree)
      case _                        => JsError("Unknown cannotClaimAsExpense")
    }
  }
}
