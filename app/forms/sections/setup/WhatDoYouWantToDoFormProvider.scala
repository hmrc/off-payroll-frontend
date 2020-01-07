/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.sections.setup

import forms.mappings.Mappings
import javax.inject.Inject
import models.sections.setup.WhatDoYouWantToDo
import play.api.data.Form

class WhatDoYouWantToDoFormProvider @Inject() extends Mappings {

  def apply(): Form[WhatDoYouWantToDo] =
    Form(
      "value" -> enumerable[WhatDoYouWantToDo]("whatDoYouWantToDo.error.required", "whatDoYouWantToDo.error.invalid")
    )
}
