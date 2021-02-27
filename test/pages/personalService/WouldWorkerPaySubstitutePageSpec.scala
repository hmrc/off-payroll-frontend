/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.personalService

import pages.behaviours.PageBehaviours
import pages.sections.personalService.WouldWorkerPaySubstitutePage

class WouldWorkerPaySubstitutePageSpec extends PageBehaviours {

  "WouldWorkerPaySubstitutePage" must {

    beRetrievable[Boolean](WouldWorkerPaySubstitutePage)

    beSettable[Boolean](WouldWorkerPaySubstitutePage)

    beRemovable[Boolean](WouldWorkerPaySubstitutePage)
  }
}
