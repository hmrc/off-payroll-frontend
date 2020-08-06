/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package models

import models.sections.financialRisk.HowWorkerIsPaid
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class HowWorkerIsPaidSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "HowWorkerIsPaid" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(HowWorkerIsPaid.values)

      forAll(gen) {
        howWorkerIsPaid =>

          JsString(howWorkerIsPaid.toString).validate[HowWorkerIsPaid].asOpt.value mustEqual howWorkerIsPaid
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!HowWorkerIsPaid.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[HowWorkerIsPaid] mustEqual JsError("Unknown howWorkerIsPaid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(HowWorkerIsPaid.values)

      forAll(gen) {
        howWorkerIsPaid =>

          Json.toJson(howWorkerIsPaid) mustEqual JsString(howWorkerIsPaid.toString)
      }
    }
  }
}
