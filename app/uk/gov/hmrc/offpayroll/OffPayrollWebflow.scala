/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.offpayroll


/**
  * Created by peter on 02/12/2016.
  *
  *
  */


object ElementType extends Enumeration {
  val RADIO = Value("radio")
}

/**
  * Represents a Cluster which is a part of an Interview in Offpayroll
  */
abstract class Cluster {

  def clusterElements: List[Element]
  def clusterID: Int

  override def toString: String = {
    " Cluster ID: " + clusterID
  }

}

abstract class Webflow {

  def getNext(element: Element):Option[Element]

  def getStart():Element

  def getEelmentById(clusterId: Int, elementId: Int): Option[Element]
}

object OffPayrollWebflow extends Webflow {


  override def getNext(element: Element):Option[Element] = {

    val clusterId = element.clusterParent.clusterID
    val cluster = clusters(clusterId)

    if(cluster.clusterElements.size > element.order + 1)
      Option(cluster.clusterElements(element.order + 1))
    else
      Option.empty[Element]

  }


  override def getStart():Element = clusters.head.clusterElements.head

  override def getEelmentById(clusterId: Int, elementId: Int): Option[Element] = {

    if (clusters.size > clusterId && clusters(clusterId).clusterElements.size > elementId) {
      Option(clusters(clusterId).clusterElements(elementId))
    }
    else
      Option.empty[Element]

  }



  val clusters: List[Cluster] = List(PersonalService)


  object PersonalService extends Cluster {

    override def clusterID: Int = 0

    val clusterElements = List(
        Element("personalService.workerSentActualSubstitiute", ElementType.RADIO, 0, this),
        Element("personalService.contractrualObligationForSubstitute", ElementType.RADIO, 1, this),
        Element("personalService.possibleSubstituteRejection", ElementType.RADIO, 2, this),
        Element("personalService.contractualRightForSubstitute", ElementType.RADIO, 3, this),
        Element("personalService.workerPayActualHelper", ElementType.RADIO, 4, this),
        Element("personalService.engagerArrangeWorker", ElementType.RADIO, 5, this),
        Element("personalService.contractTermsWorkerPaysSubstitute", ElementType.RADIO, 6, this),
        Element("personalService.workerSentActualHelper", ElementType.RADIO, 7, this),
        Element("personalService.possibleHelper", ElementType.RADIO, 8, this)
    )
  }


}


case class Element(questionTag: String, elementType: _root_.uk.gov.hmrc.offpayroll.ElementType.Value, order: Int, clusterParent: Cluster) {
  override def toString: String = {
    "Question Tag: " + questionTag + " Element Type: " + elementType + " Order: " + order + " In Cluster: " + clusterParent.toString
  }
}

