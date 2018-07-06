/*
 * Copyright 2018 HM Revenue & Customs
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
  * Created by peter on 06/01/2017.
  */
class SetupClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {

  private val setupCluster = SetupCluster

  private val setupInterview = PropertyFileLoader.transformMapFromQuestionTextToAnswers("setup")
  private val setupInterviewWithAnswers = PropertyFileLoader.transformMapToAListOfAnswers(setupInterview)

  private val personDoingWork = "setup.endUserRole" -> "setup.endUserRole.personDoingWork"
  private val lastQuestion = "setup.provideServices" -> "setup.provideServices.limitedCompany"

  val fullInterview = List(
    personDoingWork,
    "setup.hasContractStarted" -> "No",
    lastQuestion
  )

  "A Setup Cluster" should " be called setup" in {
    setupCluster.name shouldBe "setup"
  }

  it should "be identified as the first cluster with zero as its id" in {
    setupCluster.clusterID shouldBe 0
  }

  it should "have a collection of 3 cluster elements " in {
    setupCluster.clusterElements.size shouldBe 3
  }

  it should "ask the next question if not all questions have been answered " in {
    setupCluster.shouldAskForDecision(List(personDoingWork), personDoingWork).nonEmpty shouldBe true
  }

  it should "return an empty option of all questions answered" in {
    setupCluster.shouldAskForDecision(setupInterviewWithAnswers, lastQuestion).nonEmpty shouldBe false
  }

  it should "have all the questions present in the messages for setup" in {
    assertAllElementsPresentForCluster(setupCluster) shouldBe true
  }

}
