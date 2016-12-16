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

package uk.gov.hmrc.offpayroll.models

/**
  * Created by peter on 02/12/2016.
  *
  *
  */
object OffPayrollWebflow extends Webflow {

  val version: String = "0.0.1-alpha"

  def clusters: List[Cluster] = List(PersonalServiceCluster, ControlCluster)

  override def getNext(element: Element): Option[Element] = {

    val clusterId = element.clusterParent.clusterID
    val cluster = clusters()(clusterId)

    if (cluster.clusterElements.size > element.order + 1)
      Option(cluster.clusterElements(element.order + 1))
    else
      Option.empty[Element]

  }

  override def getStart(): Element = clusters.head.clusterElements.head

  override def getElementByTag(tag: String): Option[Element] = {

    def loop(cluster: List[Cluster]): Option[Element] = {
      if (cluster.isEmpty) Option.empty[Element]
      else {
        cluster.head.clusterElements.foldRight(Option.empty[Element])((element, option) => {
          if (element.questionTag == tag) Option(element)
          else option
        })
      }
    }

    loop(clusters())
  }

  override def getEelmentById(clusterId: Int, elementId: Int): Option[Element] = {

    if (clusters.size > clusterId && clusters()(clusterId).clusterElements.size > elementId) {
      Option(clusters()(clusterId).clusterElements(elementId))
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
}

object DecisionBuilder {

//  import FlowHelper._

  //  @Todo get this value from the
  val correlationID: String = "12345"

  type Interview = Map[String, String]

  def buildDecisionRequest(interview: Interview): DecisionRequest = {

    val listOfTripple = interview.toList
      .map{
        case(n,a) =>
          n match {
            case ClusterAndQuestion(c,q) => (c,(q,a))
            case _ => (n,(n,a))
          }
      }

    val fliteredByValidClusters = listOfTripple
      .filter{ case (c,n) => OffPayrollWebflow.clusters.exists(cluster => c == cluster.name)}

    val groupByCluster = fliteredByValidClusters.groupBy {
      case (cl, p) => cl
    }

    val mappedToResult = groupByCluster
      .map { case (cl, t3) => (cl, t3.map { case (t1, t2) => t2 }.toMap) }

    DecisionRequest(OffPayrollWebflow.version, correlationID, mappedToResult)
  }

}

object ClusterAndQuestion {

  def unapply(tag:String):Option[(String,String)] = {
    if (tag.split('.').length > 1) Some(tag.split('.')(0),tag.split('.')(1))
    else None
  }
}

case class Decision(qa: Map[String, String], decision: DecisionType)
