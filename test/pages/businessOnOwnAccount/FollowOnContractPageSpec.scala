/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.FollowOnContractPage

class FollowOnContractPageSpec extends PageBehaviours {

  "FollowOnContractPage" must {

    beRetrievable[Boolean](FollowOnContractPage)

    beSettable[Boolean](FollowOnContractPage)

    beRemovable[Boolean](FollowOnContractPage)
  }
}
