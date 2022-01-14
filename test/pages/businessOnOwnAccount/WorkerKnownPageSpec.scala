/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.WorkerKnownPage

class WorkerKnownPageSpec extends PageBehaviours {

  "WorkerKnownPage" must {

    beRetrievable[Boolean](WorkerKnownPage)

    beSettable[Boolean](WorkerKnownPage)

    beRemovable[Boolean](WorkerKnownPage)
  }
}
