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

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.typeDefs.Interview

/**
  * Created by peter on 05/12/2016.
  */
class OffpayrollWebflowSpec extends FlatSpec with Matchers with MockitoSugar {

  val mockClusters = mock[List[Cluster]]

  private val webflow = OffPayrollWebflow


  private val firstElement: Element = webflow.getStart()
  private val lastElement = webflow.clusters(5).clusterElements(7)


  private val personalservice = "personalService"
  private val firstQuestionTag = personalservice + ".contractualObligationForSubstitute"

  "An OffPayrollWebflow " should " start with the  PersonalServiceCluster Cluster and with Element contractualObligationForSubstitute" in {
    val startElement = webflow.getStart()
    startElement.clusterParent.name should be (personalservice)
    startElement.questionTag should be (firstQuestionTag)
  }

  it should "have a two clusters" in {
    webflow.clusters.size should be (6)
  }

  it should " be able to get a Cluster by its name " in {
    val cluster: Cluster = webflow.getClusterByName(personalservice)
    cluster.name should be (personalservice)
  }

  it should " be able to get the start currentElement as the start point for the Interview" in {
    webflow.getStart() should equal(firstElement)
  }

  val interview: Interview = Map(firstQuestionTag -> "No")


  it should " give an empty option currentElement when we try and get an currentElement that is out of bound" in {
    webflow.getNext(lastElement).isEmpty should be (true)
  }

  it should "be able to get an currentElement by id that is valid" in {
    webflow.getElementById(0, lastElement.order).nonEmpty should be (true)
    webflow.getElementById(0, 0).nonEmpty should be (true)
  }

  it should "return an empty Option if we try and get an currentElement by Id that does not exist" in {
    webflow.getElementById(6, 0).isEmpty should be (true)
    webflow.getElementById(6, lastElement.order + 1).isEmpty should be (true)
  }

  it should " be able to return an Element by its tag " in {
    val contractualRightReflectInPractice: Element = webflow.getElementById(0, 4).head
    val controlToldWhatToDo = webflow.getElementById(1,0).head

    webflow.getElementByTag(personalservice + ".contractualRightReflectInPractice")
      .head.questionTag should equal (contractualRightReflectInPractice.questionTag)

    webflow.getElementByTag("control.toldWhatToDo.yes")
      .head.questionTag should equal (controlToldWhatToDo.questionTag)


  }

}
