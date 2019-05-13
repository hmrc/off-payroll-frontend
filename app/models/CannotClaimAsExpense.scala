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
import viewmodels.{RadioOption, checkbox, radio}

sealed trait CannotClaimAsExpense

object CannotClaimAsExpense {

  case object WorkerProvidedMaterials extends WithName("workerProvidedMaterials") with CannotClaimAsExpense
  case object WorkerProvidedEquipment extends WithName("workerProvidedEquipment") with CannotClaimAsExpense
  case object WorkerUsedVehicle extends WithName("workerUsedVehicle") with CannotClaimAsExpense
  case object WorkerHadOtherExpenses extends WithName("workerHadOtherExpenses") with CannotClaimAsExpense
  case object ExpensesAreNotRelevantForRole extends WithName("expensesAreNotRelevantForRole") with CannotClaimAsExpense

  val values: Seq[CannotClaimAsExpense] = Seq(
    WorkerProvidedMaterials, WorkerProvidedEquipment, WorkerUsedVehicle, WorkerHadOtherExpenses, ExpensesAreNotRelevantForRole
  )

  val options: Seq[RadioOption] = Seq(
    RadioOption("cannotClaimAsExpense", WorkerProvidedMaterials.toString, checkbox, hasTailoredMsgs = true),
    RadioOption("cannotClaimAsExpense", WorkerProvidedEquipment.toString, checkbox, hasTailoredMsgs = true),
    RadioOption("cannotClaimAsExpense", WorkerUsedVehicle.toString, checkbox, hasTailoredMsgs = true),
    RadioOption("cannotClaimAsExpense", WorkerHadOtherExpenses.toString, checkbox, hasTailoredMsgs = true),
    RadioOption("cannotClaimAsExpense", ExpensesAreNotRelevantForRole.toString, radio, hasTailoredMsgs = true)
  )

  implicit val enumerable: Enumerable[CannotClaimAsExpense] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit object CannotClaimAsExpenseWrites extends Writes[CannotClaimAsExpense] {
    def writes(cannotClaimAsExpense: CannotClaimAsExpense) = Json.toJson(cannotClaimAsExpense.toString)
  }

  implicit object CannotClaimAsExpenseReads extends Reads[CannotClaimAsExpense] {
    override def reads(json: JsValue): JsResult[CannotClaimAsExpense] = json match {
      case JsString(WorkerProvidedMaterials.toString) => JsSuccess(WorkerProvidedMaterials)
      case JsString(WorkerProvidedEquipment.toString) => JsSuccess(WorkerProvidedEquipment)
      case JsString(WorkerUsedVehicle.toString) => JsSuccess(WorkerUsedVehicle)
      case JsString(WorkerHadOtherExpenses.toString) => JsSuccess(WorkerHadOtherExpenses)
      case JsString(ExpensesAreNotRelevantForRole.toString) => JsSuccess(ExpensesAreNotRelevantForRole)
      case _                          => JsError("Unknown cannotClaimAsExpense")
    }
  }
}
