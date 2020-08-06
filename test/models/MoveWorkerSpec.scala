/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import models.sections.control.MoveWorker
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class MoveWorkerSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "MoveWorker" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(MoveWorker.values)

      forAll(gen) {
        moveWorker =>

          JsString(moveWorker.toString).validate[MoveWorker].asOpt.value mustEqual moveWorker
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!MoveWorker.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[MoveWorker] mustEqual JsError("Unknown moveWorker")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(MoveWorker.values)

      forAll(gen) {
        moveWorker =>

          Json.toJson(moveWorker) mustEqual JsString(moveWorker.toString)
      }
    }
  }
}
