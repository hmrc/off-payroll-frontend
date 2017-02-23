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
import uk.gov.hmrc.offpayroll.util.BitHelper.{indicesToInt, msbPos}

case class ElementBitSplitter(element: Element) {
  private def encodeYesNo(value: String) = if (value.toLowerCase == "yes") 2 else if (value.toLowerCase == "no") 1 else 0

  private def encodeElementValue(value: String, element: Element): Int = {
    element.children match {
      case Nil => encodeYesNo(value)
      case children => children.find(_.questionTag == value).map(_.order + 1).getOrElse(0)
    }
  }

  private def encodeGroupElementValues(values: List[String], element: Element): (Int, Int) = {
    val indices = element.children.zipWithIndex.collect { case (child, i) if values.contains(child.questionTag) => i }
    (indicesToInt(indices), element.children.size)
  }

  def splitValues(value: String): List[String] = {
    val pattern = "\\|(.*)".r
    (value match {
      case pattern(s) => s
      case s => s
    }).split('|').toList
  }

  def bitWidth: Int = {
    if (element.children.isEmpty) 2
    else if (element.elementType == GROUP) element.children.size
    else msbPos(element.children.size + 1)
  }

  def toBitPair(value: String): (Int, Int) = {
    element.elementType match {
      case GROUP => encodeGroupElementValues(splitValues(value), element)
      case _ => (encodeElementValue(value, element), bitWidth)
    }
  }
}

object ElementBitSplitterImplicits {

  implicit def convertToBitSplitter(element: Element): ElementBitSplitter = ElementBitSplitter(element)

}
