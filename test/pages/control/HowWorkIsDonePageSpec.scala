/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.control

import models.sections.control.HowWorkIsDone
import pages.behaviours.PageBehaviours
import pages.sections.control.HowWorkIsDonePage

class HowWorkIsDonePageSpec extends PageBehaviours {

  "YourLocation" must {

    beRetrievable[HowWorkIsDone](HowWorkIsDonePage)

    beSettable[HowWorkIsDone](HowWorkIsDonePage)

    beRemovable[HowWorkIsDone](HowWorkIsDonePage)
  }
}
