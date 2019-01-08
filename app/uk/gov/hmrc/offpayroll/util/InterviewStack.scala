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

package uk.gov.hmrc.offpayroll.util

import uk.gov.hmrc.offpayroll.models.Element
import uk.gov.hmrc.offpayroll.util.ElementBitSplitterImplicits._

object InterviewStack {
  def push(previous: CompressedInterview, values: String, element: Element): CompressedInterview = {
    val pairs = previous.asValueWidthPairs
    elementIndex(element) match {
      case None => previous
      case Some(index) => CompressedInterview(insertAtIndexZeroRight(pairs, index, element.toBitPair(values)))
    }
  }

  def pop(previous: CompressedInterview): (CompressedInterview, String) = {
    val pairs = previous.asValueWidthPairs
    elementIndex(previous) match {
      case None => (previous, "")
      case Some(index) => deleteAndReadIndexZeroRight(pairs, index)
    }
  }

  def peek(previous: CompressedInterview): (CompressedInterview, String) = {
    elementIndex(previous) match {
      case None => (previous, "")
      case Some(index) =>
        (previous, getQuestionTag(index))
    }
  }

  def getQuestionTag(index: Int) = {
    ElementProvider.toElements(index).questionTag
  }

  private def insertAtIndexZeroRight(pairs: List[(Int,Int)], index: Int, pair: (Int,Int)): List[(Int,Int)] = {
    val (a,b) = pairs.splitAt(index)
    a ::: List(pair) ::: b.drop(1).map { case (_,w) => (0,w) }
  }

  private def deleteAndReadIndexZeroRight(pairs: List[(Int,Int)], index: Int): (CompressedInterview, String) = {
    val (a,b) = pairs.splitAt(index)
    val (v,_) = b.head
    val compressedInterview = CompressedInterview(a ::: b.map { case (_,w) => (0,w) })
    (compressedInterview, getQuestionTag(index))
  }

  def elementIndex(compressedInterview: CompressedInterview): Option[Int] = {
    val values = compressedInterview.asValues
    values.zipWithIndex.reverse.dropWhile{ case (a,_) => a == 0 }.headOption.map(_._2)
  }

  def elementIndex(element: Element): Option[Int] = {
    val found = ElementProvider.toElements.zipWithIndex.collect{ case (e, i) if (e == element) => i }
    found.headOption
  }

}
