/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.personalService

import pages.behaviours.PageBehaviours
import pages.sections.personalService.RejectSubstitutePage

class RejectSubstitutePageSpec extends PageBehaviours {

  "RejectSubstitutePage" must {

    beRetrievable[Boolean](RejectSubstitutePage)

    beSettable[Boolean](RejectSubstitutePage)

    beRemovable[Boolean](RejectSubstitutePage)
  }
}
