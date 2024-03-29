/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.ExtendContractPage

class ExtendContractPageSpec extends PageBehaviours {

  "ExtendContractPage" must {

    beRetrievable[Boolean](ExtendContractPage)

    beSettable[Boolean](ExtendContractPage)

    beRemovable[Boolean](ExtendContractPage)
  }
}
