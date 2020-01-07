/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.RightsOfWorkPage

class RightsOfWorkPageSpec extends PageBehaviours {

  "RightsOfWorkPage" must {

    beRetrievable[Boolean](RightsOfWorkPage)

    beSettable[Boolean](RightsOfWorkPage)

    beRemovable[Boolean](RightsOfWorkPage)
  }
}
