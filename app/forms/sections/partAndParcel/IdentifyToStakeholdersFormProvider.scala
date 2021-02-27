/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.partAndParcel

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.partAndParcel.IdentifyToStakeholders
import play.api.data.Form
import views.ViewUtils.tailorMsg

class IdentifyToStakeholdersFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[IdentifyToStakeholders] =
    Form(
      "value" -> enumerable[IdentifyToStakeholders](tailorMsg("identifyToStakeholders.error.required"))
    )
}
