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
import config.featureSwitch.OptimisedFlow
import forms.behaviours.BooleanFieldBehaviours
import forms.sections.personalService.WouldWorkerPaySubstituteFormProvider
import play.api.data.FormError

class WouldWorkerPaySubstituteFormProviderSpec extends BooleanFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "wouldWorkerPaySubstitute.error.required"
  val invalidKey = "error.required"

  val form = new WouldWorkerPaySubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    "for the sub optimised flow" should {

      disable(OptimisedFlow)
      val form = new WouldWorkerPaySubstituteFormProvider()()(fakeDataRequest, frontendAppConfig)

      behave like mandatoryField(
        form ,
        fieldName,
        requiredError = FormError(fieldName, requiredKey)
      )
    }

    "for the optimised flow" should {

      "if the user type is 'Worker'" must {

        enable(OptimisedFlow)
        val form = new WouldWorkerPaySubstituteFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.optimised.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {

        enable(OptimisedFlow)
        val form = new WouldWorkerPaySubstituteFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.optimised.$requiredKey")
        )
      }
    }
  }
}
