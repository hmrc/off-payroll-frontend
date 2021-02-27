/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.control

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.control.ChooseWhereWork
import play.api.data.Form
import views.ViewUtils.tailorMsg

class ChooseWhereWorkFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[ChooseWhereWork] =
    Form(
      "value" -> enumerable[ChooseWhereWork](tailorMsg("chooseWhereWork.error.required"))
    )
}
