/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package viewmodels

import base.SpecBase

class RadioOptionSpec extends SpecBase {

  "Radio Option" must {

    "build correctly from a key prefix and option" in {

      val radioOption = RadioOption("prefix", "option", Radio)

      radioOption.id mustEqual "prefix.option"
      radioOption.value mustEqual "option"
      radioOption.messageKey mustEqual "prefix.option"
    }
  }
}
