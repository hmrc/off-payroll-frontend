/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.businessOnOwnAccount

import pages.behaviours.PageBehaviours
import pages.sections.businessOnOwnAccount.PermissionToWorkWithOthersPage

class PermissionToWorkWithOthersPageSpec extends PageBehaviours {

  "PermissionToWorkWithOthersPage" must {

    beRetrievable[Boolean](PermissionToWorkWithOthersPage)

    beSettable[Boolean](PermissionToWorkWithOthersPage)

    beRemovable[Boolean](PermissionToWorkWithOthersPage)
  }
}
