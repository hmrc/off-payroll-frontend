/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package utils

import base.SpecBase
import config.featureSwitch.{FeatureSwitch, PrintPDF}

class FeatureSwitchSpec extends SpecBase {


  "Feature switch" must {

    "find a switch" in {

      FeatureSwitch.get("feature-switch.printPdfEnabled") mustBe Some(PrintPDF)
      FeatureSwitch.get("switch.printPdfEnabled") mustBe None

    }
    "accept a valid feature" in {
      FeatureSwitch.apply("feature-switch.printPdfEnabled") mustBe PrintPDF
    }

    "reject an invalid feature" in {
      intercept[Exception](FeatureSwitch.apply("invalid")).getMessage mustBe "Invalid feature switch: invalid"
    }
  }
}
