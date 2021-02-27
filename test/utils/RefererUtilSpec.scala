/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package utils

import base.SpecBase
import utils.RefererUtil.asRelativeUrl

class RefererUtilSpec extends SpecBase {

  "asRelativeUrl" must {

    "return a relative url for the provided absolute url" in {

      asRelativeUrl("https://www.tax.service.gov.uk/check-employment-status-for-tax/disclaimer") must
        be(Some("/check-employment-status-for-tax/disclaimer"))
    }

  }

}