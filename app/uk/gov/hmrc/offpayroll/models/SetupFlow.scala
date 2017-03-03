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
  * Created by peter on 09/01/2017.
  */
object SetupFlow extends Webflow {

  private val setupCluster = SetupCluster

  override def getNext(currentElement: Element): Option[Element] = getNext(currentElement, setupCluster)

  override def getStart(interview: Map[String, String]): Element = setupCluster.getStart(interview)

  override def getElementById(clusterId: Int, elementId: Int): Option[Element] = {
    if(clusterId == 0 && elementId < setupCluster.clusterElements.size) Option(setupCluster.clusterElements(elementId))
    else Option.empty
  }

  override def getElementByTag(tag: String): Option[Element] =
    setupCluster.clusterElements.find(element => element.questionTag == tag)

  override def clusters(): List[Cluster] = List(setupCluster)

  override def getClusterByName(name: String): Cluster =
    if(name == setupCluster.name) setupCluster
    else throw new NoSuchElementException("Named cluster " + name + " does not exist in the SetupFlow")


  def shouldAskForNext(interview: Interview, currentQnA: (String, String)): SetupResult = {
    SetupResult(setupCluster.shouldAskForDecision(interview.toList, currentQnA))
  }

}

case class SetupResult(maybeElement: Option[Element] = Option.empty, exitTool: Boolean = false)
