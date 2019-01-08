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

import uk.gov.hmrc.offpayroll.models.{Element, GROUP, MULTI, RADIO}

case class ElementBitAssembler(element: Element) {

  private def decodeMulti(bitValue: Int): String = {
    val maybeQuestionTag = element.children.find(_.order + 1 == bitValue).map(_.questionTag)
    maybeQuestionTag.getOrElse("")
  }

  private def decodeGroup(bitValue: Int): String = {
    val tags = element.children.collect { case a if (bitValue & (1 << a.order)) > 0 => a }
    tags.map(_.questionTag).mkString("|","|","")
  }

  private def decodeYesNo(bitValue: Int) = List("", "No", "Yes", "")(bitValue & 3)

  def fromBitValue(bitValue: Int): String = element.elementType match {
    case RADIO => decodeYesNo(bitValue)
    case MULTI => decodeMulti(bitValue)
    case GROUP => decodeGroup(bitValue)
  }
}

object ElementBitAssemblerImplicits {

  implicit def convertToBitAssembler(element: Element): ElementBitAssembler = ElementBitAssembler(element)

}
