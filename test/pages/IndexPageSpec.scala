/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package pages

import pages.behaviours.PageBehaviours

class IndexPageSpec extends PageBehaviours {

  "IndexPage" should {

    "have the correct name" in {
      IndexPage.toString mustBe "index"
    }
  }
}
