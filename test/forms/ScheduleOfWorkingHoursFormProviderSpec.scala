/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms

import base.GuiceAppSpecBase
import forms.behaviours.OptionFieldBehaviours
import forms.sections.control.ScheduleOfWorkingHoursFormProvider
import models.sections.control.ScheduleOfWorkingHours
import play.api.data.FormError

class ScheduleOfWorkingHoursFormProviderSpec extends OptionFieldBehaviours with GuiceAppSpecBase {

  val fieldName = "value"
  val requiredKey = "scheduleOfWorkingHours.error.required"

  val form = new ScheduleOfWorkingHoursFormProvider()()(fakeDataRequest, frontendAppConfig)

  ".value" must {

    behave like optionsField[ScheduleOfWorkingHours](
      form,
      fieldName,
      validValues  = ScheduleOfWorkingHours.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    "for the normal flow" should {

      "if the user type is 'Worker'" must {


        val form = new ScheduleOfWorkingHoursFormProvider()()(workerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"worker.$requiredKey")
        )
      }

      "if the user type is 'Hirer'" must {


        val form = new ScheduleOfWorkingHoursFormProvider()()(hirerFakeDataRequest, frontendAppConfig)

        behave like mandatoryField(
          form,
          fieldName,
          requiredError = FormError(fieldName, s"hirer.$requiredKey")
        )
      }
    }
  }
}
