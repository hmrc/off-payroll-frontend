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

package models

import models.sections.control.HowWorkIsDone
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class HowWorkIsDoneSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "HowWorkIsDone" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(HowWorkIsDone.values)

      forAll(gen) {
        howWorkIsDone =>

          JsString(howWorkIsDone.toString).validate[HowWorkIsDone].asOpt.value mustEqual howWorkIsDone
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!HowWorkIsDone.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[HowWorkIsDone] mustEqual JsError("Unknown howWorkIsDone")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(HowWorkIsDone.values)

      forAll(gen) {
        howWorkIsDone =>

          Json.toJson(howWorkIsDone) mustEqual JsString(howWorkIsDone.toString)
      }
    }
  }
}
