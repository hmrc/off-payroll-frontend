/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.control.HowWorkIsDoneFormProvider
import models.sections.control.HowWorkIsDone
import play.api.data.FormError

class HowWorkIsDoneFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "howWorkIsDone.error.required"
  val form = new HowWorkIsDoneFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like optionsField[HowWorkIsDone](
      form,
      fieldName,
      validValues  = HowWorkIsDone.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new HowWorkIsDoneFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new HowWorkIsDoneFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
