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
import models.BusinessSize
import models.BusinessSize.{BalanceSheet, Employees, NoneOfAbove, Turnover}
import play.api.data.FormError

class BusinessSizeFormProviderSpec extends OptionFieldBehaviours {

  val form = new BusinessSizeFormProvider()()

  ".value" must {

    def fieldName(i: Int = 0) = s"businessSize[$i]"

    val requiredKey = "businessSize.error.required"

    behave like optionsField[BusinessSize](
      form,
      fieldName(),
      validValues  = BusinessSize.values,
      invalidError = FormError(fieldName(), "error.invalid")
    )

    "If None Of Above is selected, remove any other ticked options that haven't been deselected by JavaScript" in {
      val result = form.bind(Map(
        fieldName() -> Turnover.toString,
        fieldName(1) -> BalanceSheet.toString,
        fieldName(2) -> Employees.toString,
        fieldName(3) -> NoneOfAbove.toString
      ))
      result.value.value shouldEqual Seq(NoneOfAbove)
    }
  }
}
