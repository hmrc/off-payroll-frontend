/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.BooleanFieldBehaviours
import forms.sections.personalService.NeededToPayHelperFormProvider
import play.api.data.FormError

class NeededToPayHelperFormProviderSpec extends BooleanFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "neededToPayHelper.error.required"
  val invalidKey = "error.required"

  val form = new NeededToPayHelperFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new NeededToPayHelperFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new NeededToPayHelperFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
