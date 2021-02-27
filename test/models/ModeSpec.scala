/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import base.SpecBase

class ModeSpec extends SpecBase {

  "Mode.jsLiteral.to()" must {
    "return CheckMode String for CheckMode" in {
      Mode.jsLiteral.to(CheckMode) mustBe "CheckMode"
    }
    "return NormalMode String for NormalMode" in {
      Mode.jsLiteral.to(NormalMode) mustBe "NormalMode"
    }
  }
}
