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
  * Created by peter on 11/01/2017.
  */
object ExitFlow extends Webflow {

  override def version: String = "1.0.1-beta"

  private val exitCluster = ExitCluster

  override def getNext(currentElement: Element): Option[Element] = getNext(currentElement, clusters(0))

  override def getStart(interview: Map[String, String]): Element = exitCluster.clusterElements(0)

  override def getElementById(clusterId: Int, elementId: Int): Option[Element] = {
    if (clusterId == 0 && elementId < clusters(0).clusterElements.size)
      Option(clusters(0).clusterElements(elementId))
    else
      Option.empty
  }

  override def getElementByTag(tag: String): Option[Element] =
    clusters.head.clusterElements.find(element => element.questionTag == tag)


  override def clusters: List[Cluster] =
    List(exitCluster)

  override def getClusterByName(name: String): Cluster =
    clusters.find(cluster => cluster.name == name).get


  def shouldAskForNext(interview: Interview, currentQnA: (String, String)): ExitResult = {

    val maybeElement = exitCluster.shouldAskForDecision(interview.toList, currentQnA)

    def isOfficeHolder: Boolean = {
      interview.exists {
        case (question, answer) => question == "exit.officeHolder" && answer.toUpperCase == "YES"
      }
    }

    def thereAreMoreQuestionsToBeAsked = {
      maybeElement.nonEmpty
    }

    if (thereAreMoreQuestionsToBeAsked) ExitResult(maybeElement)
    else {
      if (isOfficeHolder) ExitResult(inIr35 = true)
      else ExitResult()
    }
  }

}


case class ExitResult(element: Option[Element] = Option.empty, inIr35: Boolean = false)
