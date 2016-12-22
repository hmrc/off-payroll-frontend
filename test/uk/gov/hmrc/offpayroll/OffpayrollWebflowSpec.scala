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

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.models.DecisionBuilder.Interview
import uk.gov.hmrc.offpayroll.models.{Cluster, Element, OffPayrollWebflow, Webflow}

/**
  * Created by peter on 05/12/2016.
  */
class OffpayrollWebflowSpec extends FlatSpec with Matchers with MockitoSugar {

  val mockClusters = mock[List[Cluster]]

  private val webflow = OffPayrollWebflow


  private val firstElement: Element = webflow.getStart()
  private val lastElement = webflow.clusters()(0).clusterElements(8)


  private val personalservice = "personalService"
  private val firstQuestionTag = personalservice + ".contractualObligationForSubstitute"

  "An OffPayrollWebflow " should " start with the  PersonalServiceCluster Cluster and with Element contractualObligationForSubstitute" in {
    val startElement = webflow.getStart()
    startElement.clusterParent.name should be (personalservice)
    startElement.questionTag should be (firstQuestionTag)
  }

  it should "have a two clusters" in {
    webflow.clusters.size should be (2)
  }

  it should " be able to get a Cluster by its name " in {
    val cluster: Cluster = webflow.getClusterByName(personalservice)
    cluster.name should be (personalservice)
  }

  it should " be able to get the start currentElement as the start point for the Interview" in {
    webflow.getStart() should equal(firstElement)
  }

  val interview: Interview = Map(firstQuestionTag -> "No")

  it should "be able to get the next currentElement based on the current currentElement and the current interview" in {
    val next = webflow.getNext(firstElement, interview)

    next should not be(next.isEmpty)
//    next.head should equal(webflow.clusters()(0).clusterElements(1))
  }

  it should " give an empty option currentElement when we try and get an currentElement that is out of bound" in {
    webflow.getNext(lastElement).isEmpty should be (true)
  }

  it should "be able to get an currentElement by id that is valid" in {
    webflow.getEelmentById(0, lastElement.order).nonEmpty should be (true)
    webflow.getEelmentById(0, 0).nonEmpty should be (true)
  }

  it should "return an empty Option if we try and get an currentElement by Id that does not exist" in {
    webflow.getEelmentById(2, 0).isEmpty should be (true)
    webflow.getEelmentById(0, lastElement.order + 1).isEmpty should be (true)
  }

  it should " be able to return an Element by its tag " in {
    val workerPayActualHelper: Element = webflow.getEelmentById(0, 4).head

    webflow.getElementByTag(personalservice + ".workerPayActualHelper").head.questionTag should equal (workerPayActualHelper.questionTag)
  }

}
