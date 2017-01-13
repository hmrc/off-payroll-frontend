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
import uk.gov.hmrc.offpayroll.resources.{exit_conditionsLiabilityLtd7Yes, _}

/**
  * Created by peter on 08/01/2017.
  */
class ExitClusterSpec extends FlatSpec with Matchers with ClusterSpecHelper {
  val exitcluster = ExitCluster

  "An Exit Cluster " should " be named exit" in {
    exitcluster.name shouldBe "exit"
  }

  it should "be in postition 2 of the list of clusters " in {
    exitcluster.clusterID shouldBe 1
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

  it should "continue to ask ltd2 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No), exit_conditionsLiabilityLtd1No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd2
  }

  it should "continue to ask ltd3 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No), exit_conditionsLiabilityLtd2No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd3
  }

  it should "continue to ask ltd4 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No, exit_conditionsLiabilityLtd3No), exit_conditionsLiabilityLtd3No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd4
  }

  it should "continue to ask ltd5 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No), exit_conditionsLiabilityLtd4No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd5

  }

  it should "continue to ask ltd6 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No, exit_conditionsLiabilityLtd5No), exit_conditionsLiabilityLtd5No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd6

  }

  it should "continue to ask ltd7 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No, exit_conditionsLiabilityLtd5No,
      exit_conditionsLiabilityLtd6No), exit_conditionsLiabilityLtd6No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd7

  }

  it should "continue to ask ltd8 Company questions only if the previous one was no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No,
      exit_conditionsLiabilityLtd5No, exit_conditionsLiabilityLtd6No, exit_conditionsLiabilityLtd7No), exit_conditionsLiabilityLtd7No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityLtd8

  }

  it should "have no successive element if all Ltd Company Questions were no" in {

    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No,
      exit_conditionsLiabilityLtd5No, exit_conditionsLiabilityLtd6No, exit_conditionsLiabilityLtd7No, exit_conditionsLiabilityLtd8No), exit_conditionsLiabilityLtd8No)
    maybeElement.isEmpty shouldBe true

  }

  it should "have no successive element if ANY of the ltd co questions were answer in Yes" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany,
      officeHolderNo, exit_conditionsLiabilityLtd1No, exit_conditionsLiabilityLtd2No,
      exit_conditionsLiabilityLtd3No, exit_conditionsLiabilityLtd4No,
      exit_conditionsLiabilityLtd5No, exit_conditionsLiabilityLtd6No, exit_conditionsLiabilityLtd7Yes), exit_conditionsLiabilityLtd7Yes)
    maybeElement.isEmpty shouldBe true
  }

  it should "ask 1st partnership question if office holder is no and " +
    "partnership question answered yes in Setup Questions and current QnA is Office Holder No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityPartnership1
  }

  it should "continue to ask the 2nd partnership question " in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership, officeHolderNo,
      exit_conditionsLiabilityPartnership1No), exit_conditionsLiabilityPartnership1No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityPartnership2
  }

  it should "continue to ask the 3rd partnership question " in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership,
      officeHolderNo, exit_conditionsLiabilityPartnership1No, exit_conditionsLiabilityPartnership2No), exit_conditionsLiabilityPartnership2No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityPartnership3
  }

  it should "continue to ask the 4th partnership question " in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership,
      officeHolderNo, exit_conditionsLiabilityPartnership1No, exit_conditionsLiabilityPartnership2No,
      exit_conditionsLiabilityPartnership3No), exit_conditionsLiabilityPartnership3No)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe exit_conditionsLiabilityPartnership4
  }


  it should "have no successive questions if all Partnership Questions were No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership,
      officeHolderNo, exit_conditionsLiabilityPartnership1No, exit_conditionsLiabilityPartnership2No,
      exit_conditionsLiabilityPartnership3No, exit_conditionsLiabilityPartnership4No), exit_conditionsLiabilityPartnership4No)
    maybeElement.isEmpty shouldBe true
  }

  it should "have no successive questions if ANY of the partnership questions were Yes" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership,
      officeHolderNo, exit_conditionsLiabilityPartnership1No, exit_conditionsLiabilityPartnership2No,
      exit_conditionsLiabilityPartnership3Yes), exit_conditionsLiabilityPartnership3Yes)
    maybeElement.isEmpty shouldBe true
  }

    it should "should as the one and only intermediary question if intermediary answered in setp and office holder is no" in {
      val maybeElement = exitcluster.shouldAskForDecision(List(setupIntermediary, officeHolderNo), officeHolderNo)
      maybeElement.isEmpty shouldBe false
      maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityIndividualIntermediary"
    }

    it should "have no successive if the intermediary questions was Yes" in {
      val maybeElement = exitcluster.shouldAskForDecision(List(setupIntermediary, officeHolderNo), "exit.conditionsLiabilityIndividualIntermediary" -> "Yes")
      maybeElement.isEmpty shouldBe true

    }

}
