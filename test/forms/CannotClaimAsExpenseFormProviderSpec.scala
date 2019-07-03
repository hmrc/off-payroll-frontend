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

package forms

import forms.behaviours.OptionFieldBehaviours
import models.CannotClaimAsExpense
import models.CannotClaimAsExpense.{ExpensesAreNotRelevantForRole, WorkerProvidedEquipment, WorkerUsedVehicle}
import play.api.data.FormError

class CannotClaimAsExpenseFormProviderSpec extends OptionFieldBehaviours {

  val form = new CannotClaimAsExpenseFormProvider()()

  ".value" must {

    def fieldName(i: Int = 0) = s"cannotClaimAsExpense[$i]"

    behave like optionsField[CannotClaimAsExpense](
      form,
      fieldName(),
      validValues  = CannotClaimAsExpense.values,
      invalidError = FormError(fieldName(), "error.invalid")
    )

    "If Not Relevant is selected, remove any other ticked options that haven't been deselected by JavaScript" in {
      val result = form.bind(Map(
        fieldName() -> WorkerProvidedEquipment.toString,
        fieldName(1) -> WorkerUsedVehicle.toString,
        fieldName(2) -> ExpensesAreNotRelevantForRole.toString
      ))
      result.value.value mustBe Seq(ExpensesAreNotRelevantForRole)
    }
  }
}
