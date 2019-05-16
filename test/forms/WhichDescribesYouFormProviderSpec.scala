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
import models.WhichDescribesYouAnswer
import play.api.data.FormError

class WhichDescribesYouFormProviderSpec extends OptionFieldBehaviours {

  val requiredKey = "whichDescribesYou.error.required"
  val invalidKey = "whichDescribesYou.error.invalid"

  val form = new WhichDescribesYouFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like optionsField[WhichDescribesYouAnswer](
      form,
      fieldName,
      validValues = WhichDescribesYouAnswer.values,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
