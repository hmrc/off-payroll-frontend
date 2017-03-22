/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.offpayroll.util

import org.scalatest.BeforeAndAfter
import uk.gov.hmrc.play.test.UnitSpec

class FeatureSwitchSpec extends UnitSpec with BeforeAndAfter {

  "FeatureSwitch" should {

    "generate correct system property name for the feature" in {
      FeatureSwitch.systemPropertyName("test") shouldBe "feature.test"
    }

    "be ENABLED if the system property is defined as 'true'" in {
      System.setProperty("feature.test", "true")

      FeatureSwitch.forName("test").enabled shouldBe true
    }

    "be DISABLED if the system property is defined as 'false'" in {
      System.setProperty("feature.test", "false")

      FeatureSwitch.forName("test").enabled shouldBe false
    }

    "be DISABLED if the system property is undefined" in {
      System.clearProperty("feature.test")

      FeatureSwitch.forName("test").enabled shouldBe false
    }

    "support dynamic toggling" in {
      FeatureSwitch.enable(FeatureSwitch("test", enabled = true)).enabled shouldBe true
      FeatureSwitch.disable(FeatureSwitch("test", enabled = true)).enabled shouldBe false
    }

    "create BooleanFeatureSwitch when system property is a true" in {
      System.setProperty("feature.test", "true")

      FeatureSwitch.forName("test") shouldBe a[BooleanFeatureSwitch]
    }

    "create BooleanFeatureSwitch when system property is a false" in {
      System.setProperty("feature.test", "false")

      FeatureSwitch.forName("test") shouldBe a[BooleanFeatureSwitch]
    }

  }

  after {
    System.clearProperty("feature.test")
  }

}
