/*
 * Copyright 2019 HM Revenue & Customs
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
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.play.test.UnitSpec

class FeatureSwitchSpec extends UnitSpec with BeforeAndAfter {

  "FeatureSwitch" should {

    "generate correct system property name for the feature" in {
      FeatureSwitch.systemPropertyName("test") shouldBe "test"
    }

    "be enabled if the system property is defined as 'true'" in {
      System.setProperty("test", "true")
      FeatureSwitch.forName("test").enabled shouldBe true
    }

    "be disabled if the system property is defined as 'false'" in {
      System.setProperty("test", "false")
      FeatureSwitch.forName("test").enabled shouldBe false
    }

    "be disabled if the system property is not defined" in {
      System.clearProperty("test")
      FeatureSwitch.forName("test").enabled shouldBe false
    }

    "support dynamic toggling" in {
      FeatureSwitch.enable(FeatureSwitch("test", enabled = true)).enabled shouldBe true
      FeatureSwitch.disable(FeatureSwitch("test", enabled = true)).enabled shouldBe false
    }

    "create BooleanFeatureSwitch when system property is a true" in {
      System.setProperty("test", "true")
      FeatureSwitch.forName("test") shouldBe a[BooleanFeatureSwitch]
    }

    "create BooleanFeatureSwitch when system property is a false" in {
      System.setProperty("test", "false")
      FeatureSwitch.forName("test") shouldBe a[BooleanFeatureSwitch]
    }

    "convert to json from scala object" in {
      val jsValue:JsValue = Json.toJson(FeatureSwitch.enable(FeatureSwitch.forName("test")))
      (jsValue \\ "name").head.toString() shouldBe "\"test\""
      (jsValue \\ "enabled").head.toString() shouldBe "true"
    }

    "convert from json to scala object" in {
      val json = "{\"name\":\"test\",\"enabled\":true}"
      val parsed = Json.parse(json)
      val jsResult = Json.fromJson[FeatureSwitch](parsed)
      jsResult.isSuccess shouldBe true
      val switch = jsResult.get
      switch.enabled shouldBe true
    }
  }

  after {
    System.clearProperty("test")
  }

}
