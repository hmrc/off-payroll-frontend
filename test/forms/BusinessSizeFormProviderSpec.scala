package forms

import forms.behaviours.OptionFieldBehaviours
import models.BusinessSize
import play.api.data.FormError

class BusinessSizeFormProviderSpec extends OptionFieldBehaviours {

  val form = new BusinessSizeFormProvider()()

  ".value" must {

    val fieldName = "businessSize[0]"
    val requiredKey = "businessSize.error.required"

    behave like optionsField[BusinessSize](
      form,
      fieldName,
      validValues  = BusinessSize.values,
      invalidError = FormError(fieldName, "error.invalid")
    )
  }
}
