/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.financialRisk.HowWorkerIsPaidFormProvider
import models.sections.financialRisk.HowWorkerIsPaid
import play.api.data.FormError

class HowWorkerIsPaidFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val form = new HowWorkerIsPaidFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    val fieldName = "value"
    val requiredKey = "howWorkerIsPaid.error.required"

    behave like optionsField[HowWorkerIsPaid](
      form,
      fieldName,
      validValues  = HowWorkerIsPaid.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new HowWorkerIsPaidFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new HowWorkerIsPaidFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
