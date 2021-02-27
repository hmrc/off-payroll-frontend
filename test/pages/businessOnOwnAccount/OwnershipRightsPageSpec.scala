/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.OwnershipRightsPage

class OwnershipRightsPageSpec extends PageBehaviours {

  "OwnershipRightsPage" must {

    beRetrievable[Boolean](OwnershipRightsPage)

    beSettable[Boolean](OwnershipRightsPage)

    beRemovable[Boolean](OwnershipRightsPage)
  }
}
