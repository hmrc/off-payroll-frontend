/*
 * Copyright 2022 HM Revenue & Customs
 *
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
