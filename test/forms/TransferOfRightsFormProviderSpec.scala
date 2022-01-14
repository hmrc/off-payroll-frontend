/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.TransferOfRightsFormProvider
import play.api.data.FormError

class TransferOfRightsFormProviderSpec extends BooleanFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"

  val requiredKey = "transferOfRights.error.required"
  val invalidKey = "error.required"

  val form = new TransferOfRightsFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new TransferOfRightsFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new TransferOfRightsFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
