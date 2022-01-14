/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.control.ChooseWhereWorkFormProvider
import models.sections.control.ChooseWhereWork
import play.api.data.FormError

class ChooseWhereWorkFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "chooseWhereWork.error.required"

  val form = new ChooseWhereWorkFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {


    behave like optionsField[ChooseWhereWork](
      form,
      fieldName,
      validValues  = ChooseWhereWork.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new ChooseWhereWorkFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new ChooseWhereWorkFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
