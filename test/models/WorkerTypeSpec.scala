/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import models.sections.setup.WorkerType
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class WorkerTypeSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

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
