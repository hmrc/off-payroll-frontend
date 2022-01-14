/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.setup.WhoAreYouFormProvider
import models.UserAnswers
import models.sections.setup.WhatDoYouWantToFindOut.{IR35, PAYE}
import models.sections.setup.WhoAreYou
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
