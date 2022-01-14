/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.partAndParcel

import pages.behaviours.PageBehaviours
import pages.sections.partParcel.LineManagerDutiesPage

class LineManagerDutiesPageSpec extends PageBehaviours {

  "LineManagerDutiesPage" must {

    beRetrievable[Boolean](LineManagerDutiesPage)

    beSettable[Boolean](LineManagerDutiesPage)

    beRemovable[Boolean](LineManagerDutiesPage)
  }
}
