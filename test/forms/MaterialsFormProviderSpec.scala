/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.BooleanFieldBehaviours
import forms.sections.financialRisk.MaterialsFormProvider
import play.api.data.FormError

class MaterialsFormProviderSpec extends BooleanFieldBehaviours with GuiceAppSpecBase {

  val requiredKey = "materials.error.required"
  val invalidKey = "error.required"

  val form = new MaterialsFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new MaterialsFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new MaterialsFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
