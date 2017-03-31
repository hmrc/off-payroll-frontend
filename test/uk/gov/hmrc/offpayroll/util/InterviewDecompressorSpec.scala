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

import java.io.{ByteArrayOutputStream, PrintWriter}

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.util.InterviewDecompressor.using

import scala.io.Source
import scala.util.Try

class InterviewDecompressorSpec extends FlatSpec with Matchers {

  private val ID_RESOURCES_ROOT = "interviewDecompressor"
  private val IN_CSV = s"/$ID_RESOURCES_ROOT/ThreeInterviewsIn.csv"
  private val EXPECTED_OUT_CSV = s"/$ID_RESOURCES_ROOT/ThreeInterviewsOut.csv"
  private val inputSource = Source.fromInputStream(getClass.getResourceAsStream(IN_CSV))
  private val expectedCsvLines = Source.fromInputStream(getClass.getResourceAsStream(EXPECTED_OUT_CSV)).getLines().toList

  "interview decompressor" should "produce correct csv containing decompressed interviews" in {
    def decompress(baos: ByteArrayOutputStream): Try[String] = {
      InterviewDecompressor.readCompressedInterviews(inputSource).flatMap(l => using(new PrintWriter(baos)) { pw =>
        InterviewDecompressor.writeCompressedInterviews(l, pw)
      }).map(_ => baos.toString)
    }

    val tryString = using(new ByteArrayOutputStream()) {
      decompress
    }
    tryString.isSuccess shouldBe true
    val map: List[String] = Source.fromString(tryString.get).getLines().toList.map(_.trim)
    println( "boo " + map)
    map should contain theSameElementsInOrderAs expectedCsvLines.map(_.trim)
  }

}

