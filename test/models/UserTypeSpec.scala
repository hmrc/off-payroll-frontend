/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models

import models.UserType._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class UserTypeSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "UserType" must {

    "be able to be constructed from the AboutYouAnswer value" in {
      UserType(AboutYouAnswer.Worker) mustBe Worker
      UserType(AboutYouAnswer.Agency) mustBe Agency
      UserType(AboutYouAnswer.Client) mustBe Hirer
    }

    "deserialise valid values" in {
      forAll(Gen.oneOf(UserType.values)) { userType =>
        JsString(userType.toString).validate[UserType].asOpt.value mustEqual userType
      }
    }

    "fail to deserialise invalid values" in {
      val gen = arbitrary[String] suchThat (!UserType.values.map(_.toString).contains(_))
      forAll(gen) { invalidValue =>
        JsString(invalidValue).validate[UserType] mustEqual JsError("Unknown UserType")
      }
    }

    "serialise" in {
      forAll(Gen.oneOf(UserType.values)) { userType =>
        Json.toJson(userType) mustEqual JsString(userType.toString)
      }
    }
  }
}
