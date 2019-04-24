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

import base.SpecBase
import play.api.libs.json.Json

class AdditionalPdfDetailsSpec extends SpecBase {

  val testName = "Name"
  val testClient = "Client"
  val testJob = "Job"
  val testReference = "Ref"

  "Interview" must {

    "serialise to JSON correctly" when {

      "the maximum model is supplied" in {

        val model = AdditionalPdfDetails(
          completedBy = Some(testName),
          client = Some(testClient),
          job = Some(testJob),
          reference = Some(testReference)
        )

        val expected = Json.obj(
          "completedBy" -> testName,
          "client" -> testClient,
          "job" -> testJob,
          "reference" -> testReference
        )

        val actual = Json.toJson(model)

        actual mustBe expected
      }

      "the minimum model is supplied" in {

        val model = AdditionalPdfDetails()

        val expected = Json.obj()

        val actual = Json.toJson(model)

        actual mustBe expected
      }
    }


    "deserialise from JSON correctly" when {

      "the maximum model is supplied" in {

        val json = Json.obj(
          "completedBy" -> testName,
          "client" -> testClient,
          "job" -> testJob,
          "reference" -> testReference
        )

        val expected = AdditionalPdfDetails(
          completedBy = Some(testName),
          client = Some(testClient),
          job = Some(testJob),
          reference = Some(testReference)
        )

        val actual = json.as[AdditionalPdfDetails]

        actual mustBe expected
      }

      "the minimum model is supplied" in {

        val json = Json.obj()

        val expected = AdditionalPdfDetails( )

        val actual = json.as[AdditionalPdfDetails]

        actual mustBe expected
      }
    }
  }
}
