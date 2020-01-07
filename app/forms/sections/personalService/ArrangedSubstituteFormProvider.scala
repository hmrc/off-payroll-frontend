/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.sections.personalService

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.personalService.ArrangedSubstitute
import play.api.data.Form
import views.ViewUtils.tailorMsg

class ArrangedSubstituteFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[ArrangedSubstitute] =
    Form(
      "value" -> enumerable[ArrangedSubstitute](tailorMsg("arrangedSubstitute.error.required"))
    )
}
