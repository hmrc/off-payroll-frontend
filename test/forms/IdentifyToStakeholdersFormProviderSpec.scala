/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.partAndParcel.IdentifyToStakeholdersFormProvider
import models.sections.partAndParcel.IdentifyToStakeholders
import play.api.data.FormError

class IdentifyToStakeholdersFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "identifyToStakeholders.error.required"

  val form = new IdentifyToStakeholdersFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like optionsField[IdentifyToStakeholders](
      form,
      fieldName,
      validValues  = IdentifyToStakeholders.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new IdentifyToStakeholdersFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new IdentifyToStakeholdersFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
