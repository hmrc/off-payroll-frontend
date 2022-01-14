/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.control.MoveWorkerFormProvider
import models.sections.control.MoveWorker
import play.api.data.FormError

class MoveWorkerFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "moveWorker.error.required"

  val form = new MoveWorkerFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like optionsField[MoveWorker](
      form,
      fieldName,
      validValues  = MoveWorker.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new MoveWorkerFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new MoveWorkerFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
