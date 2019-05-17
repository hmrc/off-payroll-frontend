package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class BusinessSizeSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "BusinessSize" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(BusinessSize.values)

      forAll(gen) {
        businessSize =>

          JsString(businessSize.toString).validate[BusinessSize].asOpt.value mustEqual businessSize
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!BusinessSize.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[BusinessSize] mustEqual JsError("Unknown businessSize")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(BusinessSize.values)

      forAll(gen) {
        businessSize =>

          Json.toJson(businessSize) mustEqual JsString(businessSize.toString)
      }
    }
  }
}
