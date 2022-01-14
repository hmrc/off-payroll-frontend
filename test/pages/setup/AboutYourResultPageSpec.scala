/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package pages.setup

import pages.behaviours.PageBehaviours
import pages.sections.setup.AboutYourResultPage

class AboutYourResultPageSpec extends PageBehaviours {

  "AboutYourResultPage" should {

    "have the correct name" in {
      AboutYourResultPage.toString mustBe "aboutYourResult"
    }
  }
}
