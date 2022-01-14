/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.TransferOfRightsPage

class TransferOfRightsPageSpec extends PageBehaviours {

  "TransferOfRightsPage" must {

    beRetrievable[Boolean](TransferOfRightsPage)

    beSettable[Boolean](TransferOfRightsPage)

    beRemovable[Boolean](TransferOfRightsPage)
  }
}
