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

import forms.mappings.Mappings
import javax.inject.Inject
import models.CannotClaimAsExpense
import play.api.data.Form
import play.api.data.Forms.seq
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

class CannotClaimAsExpenseFormProvider @Inject() extends Mappings {

  def apply(): Form[Seq[CannotClaimAsExpense]] =
    Form(
      "cannotClaimAsExpense" -> seq(enumerable[CannotClaimAsExpense]()).verifying(validField)
    )

  def validField: Constraint[Seq[CannotClaimAsExpense]] = {

      Constraint("validCannotClaimAsExpense")({
        claimAsExpense =>
          val error =
            if(claimAsExpense.nonEmpty) {
              Nil
            } else {
              Seq(ValidationError("cannotClaimAsExpense.error.required"))
            }
          if(error.isEmpty) Valid else Invalid(error)
      })
  }
}