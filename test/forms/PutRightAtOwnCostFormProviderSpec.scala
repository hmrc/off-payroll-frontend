/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.financialRisk.PutRightAtOwnCostFormProvider
import models.sections.financialRisk.PutRightAtOwnCost
import play.api.data.FormError

class PutRightAtOwnCostFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val form = new PutRightAtOwnCostFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    val fieldName = "value"
    val requiredKey = "putRightAtOwnCost.error.required"

    behave like optionsField[PutRightAtOwnCost](
      form,
      fieldName,
      validValues  = PutRightAtOwnCost.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new PutRightAtOwnCostFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new PutRightAtOwnCostFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
