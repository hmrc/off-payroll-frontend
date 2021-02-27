/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.exit

import pages.behaviours.PageBehaviours
import pages.sections.exit.OfficeHolderPage

class OfficeHolderPageSpec extends PageBehaviours {

  "OfficeHolderPage" must {

    beRetrievable[Boolean](OfficeHolderPage)

    beSettable[Boolean](OfficeHolderPage)

    beRemovable[Boolean](OfficeHolderPage)
  }
}
