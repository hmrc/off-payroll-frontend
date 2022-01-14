/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class ExtendContractFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("extendContract.error.required")
    )
}
