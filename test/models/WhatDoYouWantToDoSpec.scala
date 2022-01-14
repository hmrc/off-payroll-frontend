/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package models

import models.sections.setup.WhatDoYouWantToDo
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class WhatDoYouWantToDoSpec extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "WhatDoYouWantToDo" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(WhatDoYouWantToDo.values)

      forAll(gen) {
        whatDoYouWantToDo =>

          JsString(whatDoYouWantToDo.toString).validate[WhatDoYouWantToDo].asOpt.value mustEqual whatDoYouWantToDo
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!WhatDoYouWantToDo.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[WhatDoYouWantToDo] mustEqual JsError("Unknown whatDoYouWantToDo")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(WhatDoYouWantToDo.values)

      forAll(gen) {
        whatDoYouWantToDo =>

          Json.toJson(whatDoYouWantToDo) mustEqual JsString(whatDoYouWantToDo.toString)
      }
    }
  }
}
