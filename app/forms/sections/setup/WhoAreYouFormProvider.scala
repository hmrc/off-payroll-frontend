/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package forms.sections.setup

import forms.mappings.Mappings
import javax.inject.Inject
import models.requests.DataRequest
import models.sections.setup.WhatDoYouWantToFindOut.IR35
import models.sections.setup.WhoAreYou
import pages.sections.setup.WhatDoYouWantToFindOutPage
import play.api.data.Form

class WhoAreYouFormProvider @Inject() extends Mappings {

  def apply()(implicit request: DataRequest[_]): Form[WhoAreYou] = {
    val journey = request.userAnswers.get(WhatDoYouWantToFindOutPage).getOrElse(IR35)
    Form(
      "value" -> enumerable[WhoAreYou](s"whoAreYou.$journey.error.required", "error.invalid")
    )
  }
}
