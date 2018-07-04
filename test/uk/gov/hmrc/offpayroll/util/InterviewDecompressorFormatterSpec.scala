/*
 * Copyright 2018 HM Revenue & Customs
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

import scala.io.Source

class InterviewDecompressorFormatterSpec extends FlatSpec with Matchers {

  private val ID_RESOURCES_ROOT = "interviewDecompressor"
  private val EXPECTED_CSV = s"/$ID_RESOURCES_ROOT/6y9FLKLlw.csv"
  val csvLines = Source.fromInputStream(getClass.getResourceAsStream(EXPECTED_CSV)).getLines().toList

  "interview decompressor formatter" should "provide correct csv line representation" in {
    InterviewDecompressorFormatter.asCsvLine("6y9FLKLlw").trim shouldBe csvLines(1)
  }

  it should "provide correct csv header representation" in {
    InterviewDecompressorFormatter.asCsvHeader("6y9FLKLlw") shouldBe csvLines(0)

  }

}
