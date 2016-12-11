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

import uk.gov.hmrc.offpayroll.views.html.interview.element


/**
  * Created by peter on 02/12/2016.
  *
  *
  */



/**
  * Represents a Cluster which is a part of an Interview in Offpayroll
  */
abstract class Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  def name:String

  /**
    * All the Elements that make up this cluster
    *
    * @return
    */
  def clusterElements: List[Element]

  /**
    * Helps order a Cluster in an Interview
    * @return
    */
  def clusterID: Int

  /**
    *
    * Based on what has been answered for this cluster should we ask
    * for a Decision
    *
    * @param clusterAnswers
    * @return
    */
  def shouldAskForDecision(clusterAnswers: List[(String, String)]):Boolean

  def shouldAskForDecision(clusterAnswers: Map[String, String]):Boolean = {
    shouldAskForDecision(
      clusterAnswers.foldLeft[List[(String, String)]](Nil)((currentList,prop) => {(prop._1, prop._2) :: currentList}))
  }

  override def toString: String = {
    " Cluster ID: " + clusterID
  }

}

abstract class Webflow {

  def getNext(element: Element):Option[Element]

  def getStart():Element

  def getEelmentById(clusterId: Int, elementId: Int): Option[Element]

  def getElementByTag(tag: String): Option[Element]

  def clusters(): List[Cluster]

  def getClusterByName(name: String): Cluster
}

object OffPayrollWebflow extends Webflow {

  def clusters: List[Cluster] = List(PersonalServiceCluster)

  override def getNext(element: Element):Option[Element] = {

    val clusterId = element.clusterParent.clusterID
    val cluster = clusters()(clusterId)

    if(cluster.clusterElements.size > element.order + 1)
    Option(cluster.clusterElements(element.order + 1))
    else
    Option.empty[Element]

  }

  override def getStart():Element = clusters.head.clusterElements.head

  override def getElementByTag(tag: String): Option[Element] = {

    def loop(cluster: List[Cluster]): Option[Element] = {
      if(cluster.isEmpty) Option.empty[Element]
      else {
        cluster.head.clusterElements.foldRight(Option.empty[Element])((element, option) => {
          if(element.questionTag == tag) Option(element)
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
    if(clusters.exists(cluster => cluster.name == name)){
      clusters.filter(cluster => cluster.name == name).head
    } else {
      throw new IllegalArgumentException("no such Cluster: " + name)
    }
  }
}

//Call this a
object PersonalServiceCluster extends Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  override def name: String = "personalService"
  override def clusterID: Int = 0

  val clusterElements: List[Element] = List(
  Element("personalService.workerSentActualSubstitiute", RADIO, 0, this),
  Element("personalService.contractrualObligationForSubstitute", RADIO, 1, this),
  Element("personalService.possibleSubstituteRejection", RADIO, 2, this),
  Element("personalService.contractualRightForSubstitute", RADIO, 3, this),
  Element("personalService.workerPayActualHelper", RADIO, 4, this),
  Element("personalService.engagerArrangeWorker", RADIO, 5, this),
  Element("personalService.contractTermsWorkerPaysSubstitute", RADIO, 6, this),
  Element("personalService.workerSentActualHelper", RADIO, 7, this),
  Element("personalService.possibleHelper", RADIO, 8, this)
  )

  override def shouldAskForDecision(clusterAnswers: List[(String, String)]): Boolean = {
    clusterElements.foldLeft[Boolean](false)((c,element) => clusterAnswers.exists(a => a._1 == element.questionTag))
  }

}


case class Element(questionTag: String, elementType: ElementType, order: Int, clusterParent: Cluster) {
  override def toString: String = {
    "Question Tag: " + questionTag + " Element Type: " + elementType + " Order: " + order + " In Cluster: " + clusterParent.toString
  }
}

trait ElementType
case object RADIO extends ElementType

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

case class Decision(qa: Map[String, String], decision: DecisionType)
