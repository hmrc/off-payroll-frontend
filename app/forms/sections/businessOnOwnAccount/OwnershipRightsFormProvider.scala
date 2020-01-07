/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.sections.businessOnOwnAccount

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class OwnershipRightsFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("ownershipRights.error.required")
    )
}
