/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.personalService

import models.sections.personalService.ArrangedSubstitute
import pages.behaviours.PageBehaviours
import pages.sections.personalService.ArrangedSubstitutePage

class ArrangedSubstitutePageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[ArrangedSubstitute](ArrangedSubstitutePage)

    beSettable[ArrangedSubstitute](ArrangedSubstitutePage)

    beRemovable[ArrangedSubstitute](ArrangedSubstitutePage)
  }
}
