/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.control

import config.FrontendAppConfig
import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.control.MoveWorker
import play.api.data.Form
import views.ViewUtils.tailorMsg

class MoveWorkerFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_], frontendAppConfig: FrontendAppConfig): Form[MoveWorker] =
    Form(
      "value" -> enumerable[MoveWorker](tailorMsg("moveWorker.error.required"))
    )
}
