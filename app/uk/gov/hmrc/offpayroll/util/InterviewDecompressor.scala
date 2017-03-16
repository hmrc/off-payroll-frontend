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

import java.io.{File, PrintWriter}

import uk.gov.hmrc.offpayroll.util.InterviewDecompressorFormatter.{asCsvHeader, asCsvLine}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object InterviewDecompressor extends App {

  if (args.length == 0) {
    println("interview decompressor - usage:")
    println("0 arguments - usage")
    println("1 argument  - compressed_interview_string (e.g. 7x0q00uiW)")
    println("2 arguments - input_file output_file")
    println("NOTE - input_file should contain single column with compressed interview strings")
    println("e.g.:")
    println("D6iw9Vb3C")
    println("6e9AH4HUm")
    println("7x0q00uiW")
    println("output_file will contain csv representations of corresponding interviews")
  }
  else if (args.length == 1){
    println(InterviewDecompressorFormatter.asMultiLine(args(0)))
  }
  else if (args.length == 2) {
    val (in, out) = (args(0), args(1))
    readCompressedInterviews(in) match {
      case Success(compressedInterviews) =>
        writeCompressedInterviews(compressedInterviews, out) match {
          case Success(s) => println(s)
          case Failure(e) => println(s"could not write to file - ${e.getMessage}")
        }
      case Failure(e) =>
        println(s"could not read compressed interviews from file - ${e.getMessage}")
    }
  }

  private def writeCompressedInterviews(compressedInterviews: List[String], file: String): Try[String] = {
    Try(using(new PrintWriter(new File(file))) { pw =>
      for ((compressedInterview, index) <- compressedInterviews.zipWithIndex) {
        if (index == 0)
          pw.println("interview, " + asCsvHeader(compressedInterview))
        pw.println(compressedInterview + ", " + asCsvLine(compressedInterview))
      }
    }).map(_ => s"done, decompressed ${compressedInterviews.size} interviews")
  }

  def readCompressedInterviews(file: String): Try[List[String]] =
    Try(Source.fromFile(file).getLines().map(_.span(_ != ',')._1.trim).toList)

  private def using[R <: { def close(): Unit }, B](resource: R)(f: R => B): B = try { f(resource) } finally { resource.close() }

}

object InterviewDecompressorFormatter {
  def asMultiLine(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asFullList
    val header = s"interview $compressedInterview:\n\n"
    val lines = for ((q, a) <- values) yield s"$q -> $a"
    header + lines.mkString("\n") + "\n"
  }

  def asCsvHeader(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asFullList
    val questions = values.map{ case(q, _) => q }
    questions.mkString(", ")
  }

  def asCsvLine(compressedInterview: String): String = {
    val values = CompressedInterview(compressedInterview).asFullList
    val answers = values.map{ case(_, a) => a.split('.').last }
    answers.mkString(", ")
  }
}
