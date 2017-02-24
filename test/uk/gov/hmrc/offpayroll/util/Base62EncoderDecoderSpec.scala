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

class Base62EncoderDecoderSpec extends FlatSpec with Matchers {

  "Base 62 encoder/decoder" should "correctly encode long into a base 62 string" in {
    Base62EncoderDecoder.encode(120163403909459L) shouldBe "Y7XjYxCV"
  }

  it should "correctly decode base 62 string into a long" in {
    Base62EncoderDecoder.decode("Y7XjYxCV") shouldBe 120163403909459L
  }

}
