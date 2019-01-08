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

package uk.gov.hmrc.offpayroll.models

import org.scalatest.{FlatSpec, Matchers}
import play.api.data.validation.{Invalid, Valid, ValidationError}

class ElementVerifierSpec extends FlatSpec with Matchers {

  "Element verifier" should "detect when element has an empty children values list" in {
    ElementVerifier.nonEmpty(List()) shouldBe Invalid(ValidationError("error.required"))
  }

  it should "detect when element has a non empty children values list" in {
    ElementVerifier.nonEmpty(List("a", "b")) shouldBe Valid
  }

  it should "detect when element is not exclusive" in {
    ElementVerifier.nonEmptyAndExclusive("a")(List("a", "b")) shouldBe Invalid(ValidationError("error.invalid.combination"))
  }

  it should "detect when element is exclusive" in {
    ElementVerifier.nonEmptyAndExclusive("a")(List("a")) shouldBe Valid
  }

  it should "detect when element is exclusive or empty" in {
    ElementVerifier.nonEmptyAndExclusive("a")(List()) shouldBe Invalid(ValidationError("error.required"))
  }

}
