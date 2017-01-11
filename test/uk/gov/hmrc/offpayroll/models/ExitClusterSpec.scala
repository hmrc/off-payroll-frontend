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

import org.scalatest
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.PropertyFileLoader

/**
  * Created by peter on 08/01/2017.
  */
class ExitClusterSpec  extends FlatSpec with Matchers with ClusterSpecHelper {
  val exitcluster = ExitCluster

  private val officeHolderProperty = "exit.officeHolder"
  private val YES = "Yes"
  private val officeHolderYes = officeHolderProperty -> YES
  private val NO = "No"
  private val officeHolderNo = officeHolderProperty -> NO

  private val setup_provideServices = "setup.provideServices"
  private val setupLtdCompany = setup_provideServices -> "setup.provideServices.limitedCompany"

  private val exit_conditionsLiabilityLtd1 = "exit.conditionsLiabilityLtd1"
  private val exit_conditionsLiabilityLtd1Yes = exit_conditionsLiabilityLtd1 -> YES

  private val exit_conditionsLiabilityLtd2 = "exit.conditionsLiabilityLtd2"
  private val exit_conditionsLiabilityLtd2Yes = exit_conditionsLiabilityLtd2 -> YES

  private val exit_conditionsLiabilityLtd7 = "exit.conditionsLiabilityLtd7"
  private val exit_conditionsLiabilityLtd7Yes = exit_conditionsLiabilityLtd7 -> YES

  val exit_conditionsLiabilityLtd8 =  "exit.conditionsLiabilityLtd8"

  val setupPartnership = setup_provideServices -> "setup.provideServices.partnership"

  val setupIntermediary = setup_provideServices -> "setup.provideServices.intermediary"



  "An Exit Cluster " should " be named exit" in {
   exitcluster.name shouldBe "exit"
 }

  it should "be in postition 2 of the list of clusters " in {
    exitcluster.clusterID  shouldBe 1
  }

  it should "have 14 questions " in {
    exitcluster.clusterElements.size shouldBe 14
  }

  it should "have all the questions present in the messages for exit" in {


    assertAllElementsPresentForCluster(exitcluster) shouldBe true
  }

  it should "allways ask for a decision if the officeHolder Question is yes " in {
    exitcluster.shouldAskForDecision(
    List(officeHolderYes), officeHolderYes).isEmpty shouldBe true

  }
  
  it should "ask 1st Limited Co question if office holder is no and " +
  "limited co question answered yes in Setup Questions and current QnA is Office Holder No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd1
  }

  it should "ask 2nd Limited Co question if office holder is no and " +
    "limited co question answered yes in Setup Questions and current QnA is: " + exit_conditionsLiabilityLtd1 in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1Yes), exit_conditionsLiabilityLtd1Yes)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd2
  }

  it should "ask 8th Limited Co question if office holder is no and " +
    "limited co question answered yes in Setup Questions and current QnA is: " + exit_conditionsLiabilityLtd7 in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1Yes, exit_conditionsLiabilityLtd2Yes), exit_conditionsLiabilityLtd7Yes)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd8
  }

  it should "ask only the partnership questions if setup.provideServices -> setup.provideServices.partnership" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityPartnership" + "1"
  }

  it should "ask the 2nd partnership questions if setup.provideServices -> setup.provideServices.partnership" +
    "and the first partnership question is asked" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership, officeHolderNo,
      "exit.conditionsLiabilityPartnership" + "1" -> "YES"), "exit.conditionsLiabilityPartnership1" -> "YES")
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityPartnership" + "2"
  }

  it should "ask only the intermediary question if setup.provideServices -> setup.provideServices.intermediary" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupIntermediary, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityIndividualIntermediary"
  }

}
