package forms

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class TransferOfRightsFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "transferOfRights.error.required"
  val invalidKey = "error.boolean"

  val form = new TransferOfRightsFormProvider()()

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
