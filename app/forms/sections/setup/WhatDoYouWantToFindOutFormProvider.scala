/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package forms.sections.setup

import forms.mappings.Mappings
import javax.inject.Inject
import models.sections.setup.WhatDoYouWantToFindOut
import play.api.data.Form

class WhatDoYouWantToFindOutFormProvider @Inject() extends Mappings {

  def apply(): Form[WhatDoYouWantToFindOut] =
    Form(
      "value" -> enumerable[WhatDoYouWantToFindOut]("whatDoYouWantToFindOut.error.required", "whatDoYouWantToFindOut.error.invalid")
    )
}
