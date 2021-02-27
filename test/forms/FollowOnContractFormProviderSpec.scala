/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.FollowOnContractFormProvider
import play.api.data.FormError

class FollowOnContractFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "followOnContract.error.required"
  val invalidKey = "error.required"

  val form = new FollowOnContractFormProvider()()

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
