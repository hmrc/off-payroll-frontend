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

package uk.gov.hmrc.offpayroll.models

object ElementVerifier {
  val basic = { l:List[String] => l.nonEmpty}
  val nonEmptyAndNotFive = { l:List[String] => l.nonEmpty && l.size < 5}
}

/**
  * Created by peter on 15/12/2016.
  */
case class Element(_questionTag: String, elementType: ElementType, order: Int, clusterParent: Cluster,
                   children: List[Element] = List(), verify:(List[String] => Boolean) = ElementVerifier.basic) {

  require(f(elementType, children), "Children only valid fot MULTI and GROUP types. There were "
    + children.size + " children and the Element Type was " + elementType)

  private def f(elementType: ElementType, children: List[Element]): Boolean = elementType match {
    case MULTI => children.nonEmpty
    case GROUP => children.nonEmpty
    case RADIO => children.isEmpty
    case CHECKBOX => children.isEmpty
    case _ => false
  }

  def questionTag: String = clusterParent.name + "." + _questionTag

  override def toString: String = {
    "Question Tag: " + questionTag + " Element Type: " + elementType + " Order: " + order + " In Cluster: " + clusterParent.toString
  }

}


trait ElementType

case object RADIO extends ElementType

case object MULTI extends ElementType

case object GROUP extends ElementType

case object CHECKBOX extends ElementType

object ElementType extends ElementType


trait DecisionType {
  val value: String
}

case object IN extends DecisionType {
  override val value: String = "decision.in.ir35"
}

case object OUT extends DecisionType {
  override val value: String = "decision.out.ir35"
}

case object UNKNOWN extends DecisionType {
  override val value: String = "decision.unknown"
}
