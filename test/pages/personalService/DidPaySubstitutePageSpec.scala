/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.personalService

import pages.behaviours.PageBehaviours
import pages.sections.personalService.DidPaySubstitutePage

class DidPaySubstitutePageSpec extends PageBehaviours {

  "DidPaySubstitutePage" must {

    beRetrievable[Boolean](DidPaySubstitutePage)

    beSettable[Boolean](DidPaySubstitutePage)

    beRemovable[Boolean](DidPaySubstitutePage)
  }
}
