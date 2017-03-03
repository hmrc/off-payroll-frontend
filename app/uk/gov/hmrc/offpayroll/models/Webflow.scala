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


/**
  * Created by peter on 15/12/2016.
  */
abstract class Webflow {

  def version  = "1.1.1-final"

  def getNext(currentElement: Element): Option[Element]

  def getStart(interview: Map[String, String]): Element

  def getElementById(clusterId: Int, elementId: Int): Option[Element]

  def getElementByTag(tag: String): Option[Element]

  def clusters: List[Cluster]

  def getClusterByName(name: String): Cluster

  def getNext(currentElement: Element, cluster: Cluster): Option[Element] = {
    val elementNumberToFind = currentElement.order + 1
    if (elementNumberToFind < cluster.clusterElements.size)
      Option(cluster.clusterElements(elementNumberToFind))
    else Option.empty
  }


}
