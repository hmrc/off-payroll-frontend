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
import models.BusinessSize
import models.BusinessSize.NoneOfAbove
import play.api.data.Form
import play.api.data.Forms.seq
import play.api.data.validation.{Constraint, Invalid, Valid}

class BusinessSizeFormProvider @Inject() extends Mappings {

  def apply(): Form[Seq[BusinessSize]] = {
    Form(
      "businessSize" -> seq(enumerable[BusinessSize]("businessSize.error.required"))
        .verifying(validFields)
        .transform[Seq[BusinessSize]](
          values => if(values.contains(NoneOfAbove)) Seq(NoneOfAbove) else values,
          x => x
        )
    )
  }

  def validFields: Constraint[Seq[BusinessSize]] = {
    Constraint[Seq[BusinessSize]]("validBusinessSize") {
      answers => if (answers.nonEmpty) Valid else Invalid("businessSize.error.required")
    }
  }
}
