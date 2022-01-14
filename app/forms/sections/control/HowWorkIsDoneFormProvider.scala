/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.control

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.control.HowWorkIsDone
import play.api.data.Form
import views.ViewUtils.tailorMsg

class HowWorkIsDoneFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[HowWorkIsDone] =
    Form(
      "value" -> enumerable[HowWorkIsDone](tailorMsg("howWorkIsDone.error.required"))
    )
}
