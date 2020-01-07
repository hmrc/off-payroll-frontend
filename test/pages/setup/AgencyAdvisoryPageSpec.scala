/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package pages.setup

import pages.behaviours.PageBehaviours
import pages.sections.setup.AgencyAdvisoryPage

class AgencyAdvisoryPageSpec extends PageBehaviours {

  "AgencyAdvisoryPage" should {

    "have the correct name" in {
      AgencyAdvisoryPage.toString mustBe "agencyAdvisoryPage"
    }
  }
}
