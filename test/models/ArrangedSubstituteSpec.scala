/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package models

import models.sections.personalService.ArrangedSubstitute
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class ArrangedSubstituteSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "ArrangedSubstitute" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(ArrangedSubstitute.values)

      forAll(gen) {
        arrangedSubstitute =>

          JsString(arrangedSubstitute.toString).validate[ArrangedSubstitute].asOpt.value mustEqual arrangedSubstitute
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!ArrangedSubstitute.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[ArrangedSubstitute] mustEqual JsError("Unknown arrangedSubstitute")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(ArrangedSubstitute.values)

      forAll(gen) {
        arrangedSubstitute =>

          Json.toJson(arrangedSubstitute) mustEqual JsString(arrangedSubstitute.toString)
      }
    }
  }
}
