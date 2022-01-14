/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.setup

import pages.behaviours.PageBehaviours
import pages.sections.setup.ContractStartedPage

class ContractStartedPageSpec extends PageBehaviours {

  "ContractStartedPage" must {

    beRetrievable[Boolean](ContractStartedPage)

    beSettable[Boolean](ContractStartedPage)

    beRemovable[Boolean](ContractStartedPage)
  }
}
