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

import uk.gov.hmrc.offpayroll.typeDefs.Interview

/**
  * Created by peter on 02/12/2016.
  *
  *
  */
object OffPayrollWebflow extends Webflow with ShouldAskForDecision {

  def clusters: List[Cluster] = List(PersonalServiceCluster, ControlCluster, FinancialRiskCluster, PartAndParcelCluster)


  override def getNext(element: Element): Option[Element] = {
    getNext(element, false)

  }

  def getNext(element: Element, nextCluster: Boolean = true) : Option[Element] = {
    val clusterId = element.clusterParent.clusterID
    val cluster = clusters(clusterId)

    def flowHasMoreClusters: Boolean = {
      clusters.size > clusterId + 1
    }

    def clusterHasMoreElements: Boolean  = {
      cluster.clusterElements.size > element.order + 1
    }

    if (!nextCluster && clusterHasMoreElements)
      Option(cluster.clusterElements(element.order + 1))
    else if (flowHasMoreClusters) {
      Option(clusters(clusterId +1).clusterElements(0))
    } else Option.empty
  }

  override def getStart(interview: Map[String, String]): Element = clusters.head.getStart(interview)

  override def getElementByTag(tag: String): Option[Element] = {

    val tagArray = tag.split('.')

    val cleanTag = if(tagArray.size > 1) tagArray(0) + "." + tagArray(1) else tag

    val foundElement = for{
      cluster <- clusters
      element <- cluster.clusterElements
      if(element.questionTag == cleanTag)
    } yield element

    // @fixme this doesn't prevent duplicates it will just return the last element found
    foundElement.headOption
  }

  override def getElementById(clusterId: Int, elementId: Int): Option[Element] = {

    if (clusters.size > clusterId && clusters(clusterId).clusterElements.size > elementId) {
      Option(clusters(clusterId).clusterElements(elementId))
    }
    else
    Option.empty[Element]
  }

  override def getClusterByName(name: String): Cluster = {
    if (clusters.exists(cluster => cluster.name == name)) {
      clusters.filter(cluster => cluster.name == name).head
    } else {
      throw new IllegalArgumentException("no such Cluster: " + name)
    }
  }

  override def shouldAskForDecision(interview: Interview, currentQnA: (String, String)): Option[Element] = {
    val currentCluster = getClusterByName(currentQnA._1.takeWhile(c => c != '.'))
    currentCluster.shouldAskForDecision(interview.toList, currentQnA)
  }
}





case class Decision(qa: Map[String, String], decision: DecisionType)
