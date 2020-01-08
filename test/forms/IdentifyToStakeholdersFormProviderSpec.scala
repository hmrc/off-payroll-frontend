/*
 * Copyright 2020 HM Revenue & Customs
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

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.partAndParcel.IdentifyToStakeholdersFormProvider
import models.sections.partAndParcel.IdentifyToStakeholders
import play.api.data.FormError

class IdentifyToStakeholdersFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "identifyToStakeholders.error.required"

  val form = new IdentifyToStakeholdersFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like optionsField[IdentifyToStakeholders](
      form,
      fieldName,
      validValues  = IdentifyToStakeholders.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new IdentifyToStakeholdersFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new IdentifyToStakeholdersFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
