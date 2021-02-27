/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class WorkerKnownFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("workerKnown.error.required")
    )
}
