/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import play.api.data.Form
import views.ViewUtils.tailorMsg

class PermissionToWorkWithOthersFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[Boolean] =
    Form(
      "value" -> boolean(tailorMsg("permissionToWorkWithOthers.error.required"))
    )
}
