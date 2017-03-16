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

object InterviewDecompressor extends App {

  println("interview decompressor - hello")

  if (args.length == 1){
    println(InterviewDecompressorFormatter.asMultiLine(args(0)))
  }
  else {
    val interviews = List("D6iw9Vb3C", "6e9AH4HUm", "7x0q00uiW")
    for (interview <- interviews) {
      val values = CompressedInterview(interview).asList
      println(s"interview $interview:")
      println
      for ((q,a) <- values){
        println(s"$q -> $a")
      }
      println
      println
    }
  }

}

object InterviewDecompressorFormatter {
  def asMultiLine(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asList
    val header = s"interview $compressedInterview:\n\n"
    val lines = for ((q, a) <- values) yield s"$q -> $a"
    header + lines.mkString("\n") + "\n"
  }

  def asCsvHeader(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asList
    val questions = values.map{ case(q, _) => q }
    questions.mkString(", ")
  }

  def asCsvLine(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asList
    val answers = values.map{ case(_, a) => a }
    answers.mkString(", ")
  }
}
