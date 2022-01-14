/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.control

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.control.ScheduleOfWorkingHours
import play.api.data.Form
import views.ViewUtils.tailorMsg

class ScheduleOfWorkingHoursFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[ScheduleOfWorkingHours] =
    Form(
      "value" -> enumerable[ScheduleOfWorkingHours](tailorMsg("scheduleOfWorkingHours.error.required"))
    )
}
