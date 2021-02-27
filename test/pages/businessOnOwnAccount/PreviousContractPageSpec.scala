/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.PreviousContractPage

class PreviousContractPageSpec extends PageBehaviours {

  "PreviousContractPage" must {

    beRetrievable[Boolean](PreviousContractPage)

    beSettable[Boolean](PreviousContractPage)

    beRemovable[Boolean](PreviousContractPage)
  }
}
