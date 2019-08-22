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
import models.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.{UserAnswers, WhoAreYou}
import pages.sections.setup.WhatDoYouWantToFindOutPage
import play.api.data.FormError

class WhoAreYouFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val ir35RequiredKey = "whoAreYou.ir35.error.required"
  val payeRequiredKey = "whoAreYou.paye.error.required"

  ".value" must {

    behave like optionsField[WhoAreYou](
      new WhoAreYouFormProvider()()(fakeDataRequest),
      fieldName,
      validValues  = WhoAreYou.values(),
      invalidError = FormError(fieldName, "error.invalid")
    )

    "if the journey type is 'IR35'" must {

      val userAnswers = UserAnswers("id").set(WhatDoYouWantToFindOutPage, IR35)
      val form = new WhoAreYouFormProvider()()(workerFakeDataRequestWithAnswers(userAnswers))

      behave like mandatoryField(
        form,
        fieldName,
        requiredError = FormError(fieldName, ir35RequiredKey)
      )
    }

    "if the journey type is 'PAYE'" must {

      val userAnswers = UserAnswers("id").set(WhatDoYouWantToFindOutPage, PAYE)
      val form = new WhoAreYouFormProvider()()(workerFakeDataRequestWithAnswers(userAnswers))

      behave like mandatoryField(
        form,
        fieldName,
        requiredError = FormError(fieldName, payeRequiredKey)
      )
    }

    "if the journey type is not set" must {

      val form = new WhoAreYouFormProvider()()(fakeDataRequest)

      behave like mandatoryField(
        form,
        fieldName,
        requiredError = FormError(fieldName, ir35RequiredKey)
      )
    }
  }
}
