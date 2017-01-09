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

import uk.gov.hmrc.offpayroll.models.DecisionBuilder.Interview

/**
  * Created by peter on 09/01/2017.
  */
object SetupFlow extends Webflow {

  private val setupCluster = SetupCluster

  override def version: String = "1.0.1-beta"

  override def getNext(currentElement: Element): Option[Element] = {
    val elementNumberToFind = currentElement.order + 1
    if (elementNumberToFind < setupCluster.clusterElements.size)
      Option(setupCluster.clusterElements(elementNumberToFind))
    else Option.empty
  }

  override def getStart(): Element = setupCluster.clusterElements(0)

  override def getEelmentById(clusterId: Int, elementId: Int): Option[Element] = {
    if(clusterId == 0 && elementId < setupCluster.clusterElements.size) Option(setupCluster.clusterElements(elementId))
    else Option.empty
  }

  override def getElementByTag(tag: String): Option[Element] =
    setupCluster.clusterElements.find(element => element.questionTag == tag)

  override def clusters(): List[Cluster] = List(setupCluster)

  override def getClusterByName(name: String): Cluster =
    if(name == setupCluster.name) setupCluster
    else throw new NoSuchElementException("Named cluster " + name + " does not exist in the SetupFlow")

  override def shouldAskForDecision(interview: Interview, currentQnA: (String, String)): Option[Element] = {
    throw new NotImplementedError("Use SetupFlow.shouldAskForNext")
  }

  def shouldAskForNext(interview: Interview, currentQnA: (String, String)): Option[Element] = {
    setupCluster.shouldAskForDecision(interview.toList, currentQnA)
  }

}
