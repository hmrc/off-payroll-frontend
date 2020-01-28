/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.partAndParcel

import models.sections.partAndParcel.IdentifyToStakeholders
import pages.behaviours.PageBehaviours
import pages.sections.partParcel.IdentifyToStakeholdersPage

class IdentifyToStakeholdersPageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[IdentifyToStakeholders](IdentifyToStakeholdersPage)

    beSettable[IdentifyToStakeholders](IdentifyToStakeholdersPage)

    beRemovable[IdentifyToStakeholders](IdentifyToStakeholdersPage)
  }
}
