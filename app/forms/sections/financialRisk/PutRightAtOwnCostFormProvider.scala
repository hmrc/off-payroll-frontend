/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.financialRisk

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.financialRisk.PutRightAtOwnCost
import play.api.data.Form
import views.ViewUtils.tailorMsg

class PutRightAtOwnCostFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[PutRightAtOwnCost] =
    Form(
      "value" -> enumerable[PutRightAtOwnCost](tailorMsg("putRightAtOwnCost.error.required"))
    )
}
