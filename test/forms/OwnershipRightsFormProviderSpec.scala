/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.OwnershipRightsFormProvider
import play.api.data.FormError

class OwnershipRightsFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "ownershipRights.error.required"
  val invalidKey = "error.required"

  val form = new OwnershipRightsFormProvider()()

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
