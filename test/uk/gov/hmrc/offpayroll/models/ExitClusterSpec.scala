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
  * Created by peter on 08/01/2017.
  */
class ExitClusterSpec  extends FlatSpec with Matchers {
  val exitcluster = ExitCluster

  private val officeHolderProperty = "exit.officeHolder"
  private val YES = "Yes"
  private val officeHolderYes = officeHolderProperty -> YES
  private val NO = "No"
  private val officeHolderNo = officeHolderProperty -> NO

  private val setupLtdCompanyProperty = "setup.provideServices.limitedCompany"
  private val setupLtdCompany = setupLtdCompanyProperty -> YES

  val setupPartnershipProperty = "setup.provideServices.partnership"
  val setupPartnership = setupPartnershipProperty -> YES

  val setupIntermediaryProperty = "setup.provideServices.intermediary"
  val setupIntermediary = setupIntermediaryProperty -> YES

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
    val allExitProperties = PropertyFileLoader.getMessagesForACluster("exit")

    allExitProperties.forall{
      case(question, value) => {
        exitcluster.clusterElements.exists( element => element.questionTag == question

          )
      }
    } shouldBe true
  }

  it should "allways ask for a decision if the officeHolder Question is yes " in {
    exitcluster.shouldAskForDecision(
    List(officeHolderYes), officeHolderYes).isEmpty shouldBe true

  }

  it should "ask Limited Co questions if office holder is no and " +
  "limited co question answered yes in Setup Questions and current QnA is Office Holder No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupLtdCompany, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityLtd1"
  }


  it should "ask Partnership questions if office holder is no and " +
    "Partnership question answered yes in Setup Questions and current QnA is Office Holder No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupPartnership, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityPartnership1"
  }

  it should "ask Intermediary questions if office holder is no and " +
    "Intermediary question answered yes in Setup Questions and current QnA is Office Holder No" in {
    val maybeElement = exitcluster.shouldAskForDecision(List(setupIntermediary, officeHolderNo), officeHolderNo)
    maybeElement.isEmpty shouldBe false
    maybeElement.get.questionTag shouldBe "exit.conditionsLiabilityIndividualIntermediary"
  }

}
