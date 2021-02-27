/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.personalService

import pages.behaviours.PageBehaviours
import pages.sections.personalService.NeededToPayHelperPage

class NeededToPayHelperPageSpec extends PageBehaviours {

  "NeededToPayHelperPage" must {

    beRetrievable[Boolean](NeededToPayHelperPage)

    beSettable[Boolean](NeededToPayHelperPage)

    beRemovable[Boolean](NeededToPayHelperPage)
  }
}
