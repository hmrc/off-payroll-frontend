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
import models.IdentifyToStakeholders
import play.api.data.FormError

class IdentifyToStakeholdersFormProviderSpec extends OptionFieldBehaviours {

  val form = new IdentifyToStakeholdersFormProvider()()

  ".value" must {

    val fieldName = "value"
    val requiredKey = "identifyToStakeholders.error.required"

    behave like optionsField[IdentifyToStakeholders](
      form,
      fieldName,
      validValues  = IdentifyToStakeholders.values(true),
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
