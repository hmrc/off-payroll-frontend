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

import base.GuiceAppSpecBase

import forms.behaviours.OptionFieldBehaviours
import forms.sections.financialRisk.PutRightAtOwnCostFormProvider
import models.sections.financialRisk.PutRightAtOwnCost
import play.api.data.FormError

class PutRightAtOwnCostFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val form = new PutRightAtOwnCostFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    val fieldName = "value"
    val requiredKey = "putRightAtOwnCost.error.required"

    behave like optionsField[PutRightAtOwnCost](
      form,
      fieldName,
      validValues  = PutRightAtOwnCost.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new PutRightAtOwnCostFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new PutRightAtOwnCostFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
