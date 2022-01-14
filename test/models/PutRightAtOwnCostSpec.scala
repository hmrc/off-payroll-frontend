/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import models.sections.financialRisk.PutRightAtOwnCost
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class PutRightAtOwnCostSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "PutRightAtOwnCost" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(PutRightAtOwnCost.values)

      forAll(gen) {
        putRightAtOwnCost =>

          JsString(putRightAtOwnCost.toString).validate[PutRightAtOwnCost].asOpt.value mustEqual putRightAtOwnCost
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!PutRightAtOwnCost.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[PutRightAtOwnCost] mustEqual JsError("Unknown putRightAtOwnCost")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(PutRightAtOwnCost.values)

      forAll(gen) {
        putRightAtOwnCost =>

          Json.toJson(putRightAtOwnCost) mustEqual JsString(putRightAtOwnCost.toString)
      }
    }
  }
}
