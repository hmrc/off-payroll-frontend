/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages

import pages.behaviours.PageBehaviours

class CheckYourAnswersPageSpec extends PageBehaviours {

  "CheckYourAnswersPage" should {

    "have the correct name" in {
      CheckYourAnswersPage.toString mustBe "checkYourAnswers"
    }
  }
}
