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

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader

/**
  * Created by peter on 03/01/2017.
  */

class ControlClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper{


  private val controlCluster = ControlCluster

  private val engagerMovingWorkerYes = "control.engagerMovingWorker" -> "Yes"
  val fullInterview = List(
    engagerMovingWorkerYes,
    "control.workerDecidingHowWorkIsDone" -> "control.workerDecidingHowWorkIsDone.noWorkerInputAllowed",
    "control.whenWorkHasToBeDone" -> "control.whenWorkHasToBeDone.workerAgreeSchedule",
    "control.workerDecideWhere" -> "control.workerDecideWhere.workerChooses"
  )

  "A Control Cluster" should " be called control" in {
    controlCluster.name shouldBe "control"
  }

  it should "have a list of Elements " in {
    controlCluster.clusterElements.size shouldBe 4
  }

  it should "have an ID of 1 (the second cluster in the flow" in {
    controlCluster.clusterID shouldBe 3
  }

  it should "have all elements in a cluster compared to messages " in {
    assertAllElementsPresentForCluster(controlCluster)
  }

  it should "work out if all questions have been answered" in {
    controlCluster.allQuestionsAnswered(fullInterview) shouldBe true
  }

  it should "give the next question to be asked when interview is not complete" in {
    controlCluster.shouldAskForDecision(List(engagerMovingWorkerYes), engagerMovingWorkerYes).isEmpty shouldBe false
  }

  it should " have the correct set of questions" in {
    assertAllElementsPresentForCluster(controlCluster) shouldBe true
  }

}
