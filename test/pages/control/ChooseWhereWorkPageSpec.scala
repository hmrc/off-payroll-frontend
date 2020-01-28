/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.control

import models.sections.control.ChooseWhereWork
import pages.behaviours.PageBehaviours
import pages.sections.control.ChooseWhereWorkPage

class ChooseWhereWorkPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[ChooseWhereWork](ChooseWhereWorkPage)

    beSettable[ChooseWhereWork](ChooseWhereWorkPage)

    beRemovable[ChooseWhereWork](ChooseWhereWorkPage)
  }
}
