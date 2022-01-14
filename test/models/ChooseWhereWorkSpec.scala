/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import models.sections.control.ChooseWhereWork
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class ChooseWhereWorkSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "ChooseWhereWork" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(ChooseWhereWork.values)

      forAll(gen) {
        chooseWhereWork =>

          JsString(chooseWhereWork.toString).validate[ChooseWhereWork].asOpt.value mustEqual chooseWhereWork
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!ChooseWhereWork.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[ChooseWhereWork] mustEqual JsError("Unknown chooseWhereWork")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(ChooseWhereWork.values)

      forAll(gen) {
        chooseWhereWork =>

          Json.toJson(chooseWhereWork) mustEqual JsString(chooseWhereWork.toString)
      }
    }
  }
}
