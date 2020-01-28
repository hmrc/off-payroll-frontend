/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.ExtendContractFormProvider
import play.api.data.FormError

class ExtendContractFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "extendContract.error.required"
  val invalidKey = "error.required"

  val form = new ExtendContractFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
