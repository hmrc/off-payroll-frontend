/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.FirstContractPage

class FirstContractPageSpec extends PageBehaviours {

  "FirstContractPage" must {

    beRetrievable[Boolean](FirstContractPage)

    beSettable[Boolean](FirstContractPage)

    beRemovable[Boolean](FirstContractPage)
  }
}
