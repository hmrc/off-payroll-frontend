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
import uk.gov.hmrc.offpayroll.util.ClusterAndQuestion

/**
  * Created by peter on 11/01/2017.
  *
  * TODO inject the WebFlow?
  */
object DecisionBuilder {

  def decodeMultipleValues(m: Map[String,String]): Map[String,String] =
    m.toList.flatMap { case (a,b) =>
      b.split('|') match {
        case s if s.size == 1 => List((a,b))
        case s => s.drop(1).map(c => (c,"Yes"))
      }
    }.toMap

  def buildDecisionRequest(interview: Interview, correlationId: String): DecisionRequest = {


    def normalizeAnswer(answer: String) = {
      answer.split('.').last
    }
    val listOfTriple = {
      decodeMultipleValues(interview).toList
        .map {
          case (n, a) =>
            n match {
              case ClusterAndQuestion(c, q) => (c, (q, a))
              case _ => (n, (n, a))
            }
        }
    }

    val fliteredByValidClusters = listOfTriple
      .filter{ case (c,n) => OffPayrollWebflow.clusters.exists(cluster => c == cluster.name)}

    val normalizeChildren = fliteredByValidClusters.map{
      case (cluster,(question,answer)) => (cluster,(question,normalizeAnswer(answer)))
    }

    val groupByCluster = normalizeChildren.groupBy {
      case (cl, p) => cl
    }

    val mappedToResult = groupByCluster
      .map { case (cl, t3) => (cl, t3.map { case (t1, t2) => t2 }.toMap) }

    DecisionRequest(OffPayrollWebflow.version, correlationId, mappedToResult)
  }

}
