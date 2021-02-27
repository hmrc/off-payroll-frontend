/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.OptionFieldBehaviours
import forms.sections.setup.WhatDoYouWantToDoFormProvider
import models.sections.setup.WhatDoYouWantToDo
import play.api.data.FormError

class WhatDoYouWantToDoFormProviderSpec extends OptionFieldBehaviours {

  val requiredKey = "whatDoYouWantToDo.error.required"
  val invalidKey = "whatDoYouWantToDo.error.invalid"

  val form = new WhatDoYouWantToDoFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like optionsField[WhatDoYouWantToDo](
      form,
      fieldName,
      validValues = WhatDoYouWantToDo.values,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
