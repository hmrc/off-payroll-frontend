/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages.partAndParcel

import pages.behaviours.PageBehaviours
import pages.sections.partParcel.BenefitsPage

class BenefitsPageSpec extends PageBehaviours {

  "BenefitsPage" must {

    beRetrievable[Boolean](BenefitsPage)

    beSettable[Boolean](BenefitsPage)

    beRemovable[Boolean](BenefitsPage)
  }
}
