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

import com.kenshoo.play.metrics.PlayModule
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.resources._
import uk.gov.hmrc.offpayroll.util.TestConfigurationHelper.getString
import uk.gov.hmrc.play.test.WithFakeApplication

/**
  * Created by peter on 05/12/2016.
  */
class OffPayrollWebflowSpec extends FlatSpec with Matchers with MockitoSugar with WithFakeApplication  {

  val mockClusters = mock[List[Cluster]]

  private val webflow = OffPayrollWebflow

  private val lastElement = webflow.clusters(5).clusterElements(3)

  override def bindModules = Seq(new PlayModule)



  private val personalService = "personalService"
  private val firstPersonalServiceQuestionTag = personalService + ".workerSentActualSubstitute"
  private val setup = "setup"
  private val firstQuestionTag = "setup.endUserRole"

  "An OffPayrollWebflow " should " start with the SetupCluster and with Element endUserRole" in {
    val startElement = webflow.getStart(Map())
    startElement.isDefined shouldBe true

    startElement.get.clusterParent.name should be (setup)
    startElement.get.questionTag should be (firstQuestionTag)
  }

  it should "have a four clusters" in {
    webflow.clusters.size should be (6)
  }

  it should " be able to get a Cluster by its name " in {
    val cluster: Cluster = webflow.getClusterByName(personalService)
    cluster.name should be (personalService)
  }

//  it should " give an empty option element when we try and get an element that is out of bound" in {
//    webflow.getNext(lastElement).isEmpty should be (true)
//  }

//  it should " give the correct next element when cluster has no more elements but flow has more clusters" in {
//    val maybeElement = webflow.getNext(webflow.clusters(3).clusterElements(3))
//    maybeElement.isEmpty should be (false)
//    maybeElement.get.clusterParent.name should be ("financialRisk")
//    maybeElement.get.questionTag should be ("financialRisk.haveToPayButCannotClaim")
//  }

  it should "be able to get a currentElement by id that is valid" in {
    checkElement(webflow.getElementById(0, 0), "setup.endUserRole")
    checkElement(webflow.getElementById(0, 1), "setup.hasContractStarted")
    checkElement(webflow.getElementById(2, 3), "personalService.possibleSubstituteWorkerPay")
    checkElement(webflow.getElementById(2, 0), "personalService.workerSentActualSubstitute")
  }

  it should "return an empty Option if we try and get a currentElement by Id that does not exist" in {
    webflow.getElementById(6, 0).isEmpty should be (true)
    webflow.getElementById(6, lastElement.order + 1).isEmpty should be (true)
  }

  it should " be able to return an Element by its tag " in {
    val wouldWorkerPayHelper: Element = webflow.getElementById(2, 4).head
    val engagerMovingWorker = webflow.getElementById(3,0).head

    webflow.getElementByTag(personalService + ".wouldWorkerPayHelper")
      .head.questionTag should equal (wouldWorkerPayHelper.questionTag)

    webflow.getElementByTag("control.engagerMovingWorker.canMoveWorkerWithPermission")
      .head.questionTag should equal (engagerMovingWorker.questionTag)

  }

  it should "have the same version as declared in application.conf" in {
    webflow.version shouldBe getString("microservice.services.off-payroll-decision.version")
  }

  def checkElement(element: Option[Element], questionTagToMatch: String): Unit ={
    element.nonEmpty shouldBe true
    element.get.questionTag shouldBe questionTagToMatch
  }

}
