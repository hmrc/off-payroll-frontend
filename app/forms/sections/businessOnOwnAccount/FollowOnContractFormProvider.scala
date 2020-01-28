/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class FollowOnContractFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("followOnContract.error.required")
    )
}
