/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.personalService.ArrangedSubstituteFormProvider
import models.sections.personalService.ArrangedSubstitute
import play.api.data.FormError

class ArrangedSubstituteFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "arrangedSubstitute.error.required"

  ".value" must {

    behave like optionsField[ArrangedSubstitute](
      new ArrangedSubstituteFormProvider()()(fakeDataRequest, frontendAppConfig),
      fieldName,
      validValues  = ArrangedSubstitute.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new ArrangedSubstituteFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new ArrangedSubstituteFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
