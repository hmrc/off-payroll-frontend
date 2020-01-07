/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import models.sections.control.HowWorkIsDone
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
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
