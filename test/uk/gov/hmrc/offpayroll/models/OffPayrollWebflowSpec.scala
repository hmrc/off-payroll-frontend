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
import uk.gov.hmrc.offpayroll.resources._

/**
  * Created by peter on 05/12/2016.
  */
class OffPayrollWebflowSpec extends FlatSpec with Matchers with MockitoSugar {

  val mockClusters = mock[List[Cluster]]

  private val webflow = OffPayrollWebflow

  private val lastElement = webflow.clusters(3).clusterElements(3)



  private val personalService = "personalService"
  private val firstQuestionTag = personalService + ".workerSentActualSubstitute"

  "An OffPayrollWebflow " should " start with the  PersonalServiceCluster Cluster and with Element workerSentActualSubstitute" in {
    val startElement = webflow.getStart(partialInterview_hasContractStarted_Yes)
    startElement.isDefined shouldBe true

    startElement.get.clusterParent.name should be (personalService)
    startElement.get.questionTag should be (firstQuestionTag)
  }

  it should "have a four clusters" in {
    webflow.clusters.size should be (4)
  }

  it should " be able to get a Cluster by its name " in {
    val cluster: Cluster = webflow.getClusterByName(personalService)
    cluster.name should be (personalService)
  }

  it should " give an empty option element when we try and get an element that is out of bound" in {
    webflow.getNext(lastElement).isEmpty should be (true)
  }

  it should " give the correct next element when cluster has no more elements but flow has more clusters" in {
    val maybeElement = webflow.getNext(webflow.clusters(1).clusterElements(3))
    maybeElement.isEmpty should be (false)
    maybeElement.get.clusterParent.name should be ("financialRisk")
    maybeElement.get.questionTag should be ("financialRisk.haveToPayButCannotClaim")
  }

  it should "be able to get a currentElement by id that is valid" in {
    webflow.getElementById(0, 3).nonEmpty should be (true)
    webflow.getElementById(0, 0).nonEmpty should be (true)
  }

  it should "return an empty Option if we try and get a currentElement by Id that does not exist" in {
    webflow.getElementById(6, 0).isEmpty should be (true)
    webflow.getElementById(6, lastElement.order + 1).isEmpty should be (true)
  }

  it should " be able to return an Element by its tag " in {
    val wouldWorkerPayHelper: Element = webflow.getElementById(0, 4).head
    val engagerMovingWorker = webflow.getElementById(1,0).head

    webflow.getElementByTag(personalService + ".wouldWorkerPayHelper")
      .head.questionTag should equal (wouldWorkerPayHelper.questionTag)

    webflow.getElementByTag("control.engagerMovingWorker.canMoveWorkerWithPermission")
      .head.questionTag should equal (engagerMovingWorker.questionTag)

  }

  it should "be at version 1.1.1-final" in {
    webflow.version shouldBe "1.1.1-final"
  }

}
