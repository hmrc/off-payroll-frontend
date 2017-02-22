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

import uk.gov.hmrc.offpayroll.models._

import scala.annotation.tailrec
import scala.collection.immutable.BitSet

object InterviewBitSplitter {
  def msbPos(n: Int): Int = {
    @tailrec
    def go(n: Int, acc: Int): Int = {
      if (n == 0) acc else go(n >> 1, acc + 1)
    }
    go(n, 0)
  }

  def indicesToInt(b: List[Int]): Int = b.foldLeft(0)((a,i) => 1 << i | a)

  private def decodeMulti(bitValue: Int, element: Element): List[String] = {
    val maybeQuestionTag = element.children.find(_.order + 1 == bitValue).map(_.questionTag)
    maybeQuestionTag.toList
  }

  private def decodeGroup(bitValue: Int, element: Element): List[String] = {
    val tags = element.children.collect { case a if (bitValue & (1 << a.order)) > 0 => a }
    tags.map(_.questionTag)
  }

  private def decodeYesNo(bitValue: Int) = List(Nil, List("No"), List("Yes"), Nil)(bitValue & 0x11)

  private def encodeYesNo(value: String) = if (value.toLowerCase == "yes") 2 else 1

  private def encodeElementValue(value: String, element: Element):Int = {
    element.children match {
      case Nil => encodeYesNo(value)
      case children => children.find(_.questionTag == value).map(_.order + 1).getOrElse(0)
    }
  }

  private def encodeGroupElementValues(values: List[String], element: Element): (Int, Int) = {
    val indices = element.children.zipWithIndex.collect{ case (child, i) if values.contains(child.questionTag) => i }
    (indicesToInt(indices), element.children.size)
  }

  private def elementBitWidth(element: Element): Int = {
    if (element.children.isEmpty) 2
    else if (element.elementType == GROUP) element.children.size
    else msbPos(element.children.size + 1)
  }

  def toBitPair(values: List[String], element: Element): (Int,Int) = {
    element.elementType match {
      case GROUP => encodeGroupElementValues(values, element)
      case _ => (encodeElementValue(values.headOption.getOrElse(""), element), elementBitWidth(element))
    }
  }

  def toElements: List[Element] = SuperWebflow.elements

  def toWidths: List[Int] = toElements.map(elementBitWidth)

  def fromBitElement(bitValue: Int, element: Element): List[String] = element.elementType match {
    case RADIO => decodeYesNo(bitValue)
    case MULTI => decodeMulti(bitValue, element)
    case GROUP => decodeGroup(bitValue, element)
  }
}
