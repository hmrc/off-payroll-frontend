/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import play.api.data.Form
import views.ViewUtils.tailorMsg

class TransferOfRightsFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[Boolean] =
    Form(
      "value" -> boolean(tailorMsg("transferOfRights.error.required"))
    )
}
