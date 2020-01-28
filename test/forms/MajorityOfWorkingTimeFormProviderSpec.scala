/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.BooleanFieldBehaviours
import forms.sections.businessOnOwnAccount.MajorityOfWorkingTimeFormProvider
import play.api.data.FormError

class MajorityOfWorkingTimeFormProviderSpec extends BooleanFieldBehaviours with GuiceAppSpecBase {

  val requiredKey = "majorityOfWorkingTime.error.required"
  val invalidKey = "error.required"

  val form = new MajorityOfWorkingTimeFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new MajorityOfWorkingTimeFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new MajorityOfWorkingTimeFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
