/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.financialRisk

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.financialRisk.HowWorkerIsPaid
import play.api.data.Form
import views.ViewUtils.tailorMsg

class HowWorkerIsPaidFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[HowWorkerIsPaid] =
    Form(
      "value" -> enumerable[HowWorkerIsPaid](tailorMsg("howWorkerIsPaid.error.required"))
    )
}
