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
    CompressedInterview("Y7XjYxCV").asLong shouldBe 120163403909459L
  }

  it should "be possible to create it from long" in {
    Base62EncoderDecoder.decode("Y7XjYxCV") shouldBe 120163403909459L
    CompressedInterview(120163403909459L).str shouldBe "Y7XjYxCV"
  }

  it should "provide value/width pairs representation" in {
    val pairs = CompressedInterview("146rjrJz").asValueWidthPairs
    pairs should contain theSameElementsInOrderAs List((3,3), (1,2), (2,2), (2,2), (2,2), (2,3), (3,3), (3,3), (4,3), (0x11,5), (4,3), (4,3), (2,2), (2,2), (2,2), (3,3))
  }

  it should "instantiate from value/width pairs" in {
    val pairs = List((3, 3), (1, 2), (2, 2), (2, 2), (2, 2), (2, 3), (3, 3), (3, 3), (4, 3), (0x11, 5), (4, 3), (4, 3), (2, 2), (2, 2), (2, 2), (3, 3))
    val compressedInterview = CompressedInterview(pairs)
    compressedInterview.str shouldBe "146rjrJz"
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
}
