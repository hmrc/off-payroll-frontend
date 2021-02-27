/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.MajorityOfWorkingTimePage

class MajorityOfWorkingTimePageSpec extends PageBehaviours {

  "MajorityOfWorkingTimePage" must {

    beRetrievable[Boolean](MajorityOfWorkingTimePage)

    beSettable[Boolean](MajorityOfWorkingTimePage)

    beRemovable[Boolean](MajorityOfWorkingTimePage)
  }
}
