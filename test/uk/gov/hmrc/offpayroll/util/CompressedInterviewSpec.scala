/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}

class CompressedInterviewSpec extends FlatSpec with Matchers {

  "compressed interview" should "provide long representation" in {
    CompressedInterview("7x7Yk4RPP").asLong shouldBe 1736585431796051L
  }

  it should "be possible to create it from long" in {
    Base62EncoderDecoder.decode("7x7Yk4RPP") shouldBe 1736585431796051L
    CompressedInterview(1736585431796051L).str shouldBe "7x7Yk4RPP"
  }

  it should "provide value/width pairs representation" in {
    val pairs = CompressedInterview("7x7Yk4RPP").asValueWidthPairs
    pairs should contain theSameElementsInOrderAs List((1, 3), (2, 2), (1, 3), (1, 2), (3,3), (1,2), (2,2), (2,2), (2,2), (2,3), (3,3), (3,3), (4,3), (0x11,5), (4,3), (4,3), (2,2), (2,2), (2,2), (3,3))
  }

  it should "instantiate from value/width pairs" in {
    val pairs = List((1, 3), (2, 2), (1, 3), (1, 2), (3, 3), (1, 2), (2, 2), (2, 2), (2, 2), (2, 3), (3, 3), (3, 3), (4, 3), (0x11, 5), (4, 3), (4, 3), (2, 2), (2, 2), (2, 2), (3, 3))
    val compressedInterview = CompressedInterview(pairs)
    compressedInterview.str shouldBe "7x7Yk4RPP"
    val p = compressedInterview.asValueWidthPairs
    p should contain theSameElementsInOrderAs pairs
  }

  it should "instantiate from empty string" in {
    val compressedInterview = CompressedInterview("")
    compressedInterview.asLong shouldBe 0L
  }

  it should "instantiate from a string '0'" in {
    val compressedInterview = CompressedInterview("0")
    compressedInterview.asLong shouldBe 0L
  }

  it should "provide list of all values" in {
    val values = CompressedInterview("7x7Yk4RPP").asValues
    values should contain theSameElementsInOrderAs List(1, 2, 1, 1, 3, 1, 2, 2, 2, 2, 3, 3, 4, 17, 4, 4, 2, 2, 2, 3)
  }

  it should "provide map from question tags to values" in {
    val interview = CompressedInterview("7x7Yk4RPP")
    val values = interview.asMap
    values.size shouldBe 21
    values("financialRisk.workerProvidedMaterials") shouldBe "Yes"
    values("financialRisk.expensesAreNotRelevantForRole") shouldBe "Yes"
  }

  it should "provide list from question tags to values" in {
    val interview = CompressedInterview("7x7Yk4RPP")
    val values = interview.asList
    values.size shouldBe 21
    values should contain (("financialRisk.workerProvidedMaterials", "Yes"))
    values should contain (("financialRisk.expensesAreNotRelevantForRole", "Yes"))
  }

  it should "provide map from question tags to values for an empty interview" in {
    val interview = CompressedInterview("")
    val values = interview.asMap
    val allAnswersEmpty = values.forall{ case (_,a) => (a.isEmpty || a == "|") }
    allAnswersEmpty shouldBe true
  }

  it should "provide list from question tags to values for an empty interview" in {
    val interview = CompressedInterview("")
    val values = interview.asList
    val allAnswersEmpty = values.forall{ case (_,a) => (a.isEmpty || a == "|") }
    allAnswersEmpty shouldBe true
  }

  it should "decode multiple values" in {
    val out = Map(
      "financialRisk.workerProvidedMaterials" -> "Yes",
      "financialRisk.workerProvidedEquipment" -> "Yes")

    CompressedInterview.decodeMultipleValues(
      Map("financialRisk.haveToPayButCannotClaim" -> "|financialRisk.workerProvidedMaterials|financialRisk.workerProvidedEquipment")
    ) shouldBe out
  }
}

