/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.financialRisk

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import play.api.data.Form
import views.ViewUtils.tailorMsg

class VehicleFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[Boolean] =
    Form(
      "value" -> boolean(tailorMsg("vehicle.error.required"))
    )
}
