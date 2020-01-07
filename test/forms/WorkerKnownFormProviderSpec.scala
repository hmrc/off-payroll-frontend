/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.WorkerKnownFormProvider
import play.api.data.FormError

class WorkerKnownFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "workerKnown.error.required"
  val invalidKey = "error.required"

  val form = new WorkerKnownFormProvider()()

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
