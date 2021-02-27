/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.MultipleContractsPage

class MultipleContractsPageSpec extends PageBehaviours {

  "MultipleContractsPage" must {

    beRetrievable[Boolean](MultipleContractsPage)

    beSettable[Boolean](MultipleContractsPage)

    beRemovable[Boolean](MultipleContractsPage)
  }
}
