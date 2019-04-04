package forms

import forms.behaviours.OptionFieldBehaviours
import models.$className$
import play.api.data.FormError

class $className$FormProviderSpec extends OptionFieldBehaviours {

  val form = new $className$FormProvider()()

  ".value" must {

    val fieldName = "$checkboxKey$[0]"
    val requiredKey = "$className;format="decap"$.error.required"

    behave like optionsField[$className$](
      form,
      fieldName,
      validValues  = $className$.values,
      invalidError = FormError(fieldName, "error.invalid")
    )
  }
}
