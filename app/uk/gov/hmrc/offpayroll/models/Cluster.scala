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

import uk.gov.hmrc.offpayroll.models.DecisionBuilder.Interview

/**
  * Represents a Cluster which is a part of an Interview in Offpayroll
  */
abstract class Cluster {

  /**
    * Use this value to informatively name the cluster and use as a key to tags
    */
  def name: String

  /**
    * All the Elements that make up this cluster
    *
    * @return
    */
  def clusterElements: List[Element]


   def makeMapFromClusterElements: Map[String, Element] = {
    Map() ++ (clusterElements map { element => (element.questionTag, element) })
  }

  /**
    * Helps order a Cluster in an Interview
    *
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
  def shouldAskForDecision(clusterAnswers: List[(String, String)], currentQnA: (String, String)): Option[Element]


  override def toString: String = {
    "{Cluster ID: " + clusterID + " Name: " + name + "}"
  }

}
