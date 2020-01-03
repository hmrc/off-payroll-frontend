/*
 * Copyright 2020 HM Revenue & Customs
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

import models.sections.setup.WorkerType
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class WorkerTypeSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "WorkerType" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(WorkerType.values)

      forAll(gen) {
        workerType =>

          JsString(workerType.toString).validate[WorkerType].asOpt.value mustEqual workerType
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!WorkerType.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[WorkerType] mustEqual JsError("Unknown workerType")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(WorkerType.values)

      forAll(gen) {
        workerType =>

          Json.toJson(workerType) mustEqual JsString(workerType.toString)
      }
    }
  }
}
