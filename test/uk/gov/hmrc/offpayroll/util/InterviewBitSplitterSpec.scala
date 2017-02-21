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

package uk.gov.hmrc.offpayroll.util

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.hmrc.offpayroll.models.{FinancialRiskCluster, PersonalServiceCluster}

class InterviewBitSplitterSpec extends FlatSpec with Matchers {

  val exampleAnswerSet = List (
    /* 0: cluster 0 children 3 type MULTI */   List("personalService.workerSentActualSubstitute.noSubstitutionHappened"),
    /* 1: cluster 0 children 0 type RADIO */   List("No"),
    /* 2: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 3: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 4: cluster 0 children 0 type RADIO */   List("Yes"),
    /* 5: cluster 1 children 3 type MULTI */   List("control.engagerMovingWorker.canMoveWorkerWithoutPermission"),
    /* 6: cluster 1 children 4 type MULTI */   List("control.workerDecidingHowWorkIsDone.noWorkerInputAllowed"),
    /* 7: cluster 1 children 4 type MULTI */   List("control.whenWorkHasToBeDone.scheduleDecidedForWorker"),
    /* 8: cluster 1 children 4 type MULTI */   List("control.workerDecideWhere.noLocationRequired"),
    /* 9: cluster 2 children 5 type GROUP */   List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"),
    /* 10: cluster 2 children 5 type MULTI */  List("financialRisk.workerMainIncome.incomeCommission"),
    /* 11: cluster 2 children 5 type MULTI */  List("financialRisk.paidForSubstandardWork.noObligationToCorrect"),
    /* 12: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 13: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 14: cluster 3 children 0 type RADIO */  List("Yes"),
    /* 15: cluster 3 children 3 type MULTI */  List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
  )

  "InterviewCompressor" should "convert all elements into list of (bit value, bit width) pairs" in {
    val encodedValues = InterviewBitSplitter.toBitPairs(exampleAnswerSet)
    encodedValues should contain theSameElementsInOrderAs List((3,3), (1,2), (2,2), (2,2), (2,2), (2,3), (3,3), (3,3), (4,3), (0x11,5), (4,3), (4,3), (2,2), (2,2), (2,2), (3,3))
  }

  "InterviewCompressor" should "convert elements width list" in {
    val widths = InterviewBitSplitter.toWidths
    widths should contain theSameElementsInOrderAs List(3, 2, 2, 2, 2, 3, 3, 3, 3, 5, 3, 3, 2, 2, 2, 3)
  }

  it should "convert single element into a (bit value, bit width) pair" in {
    val element1 = PersonalServiceCluster.clusterElements(0)
    val element2 = FinancialRiskCluster.clusterElements(1)
    val encodedValue1 = InterviewBitSplitter.toBitPair(List("personalService.workerSentActualSubstitute.yesClientAgreed"), element1)
    val encodedValue2 = InterviewBitSplitter.toBitPair(List("personalService.workerSentActualSubstitute.notAgreedWithClient"), element1)
    val encodedValue3 = InterviewBitSplitter.toBitPair(List("personalService.workerSentActualSubstitute.noSubstitutionHappened"), element1)
    val encodedValue4 = InterviewBitSplitter.toBitPair(List("financialRisk.workerMainIncome.incomeFixed"), element2)
    encodedValue1 shouldBe ((1,3):(Int,Int))
    encodedValue2 shouldBe ((2,3):(Int,Int))
    encodedValue3 shouldBe ((3,3):(Int,Int))
    encodedValue4 shouldBe ((2,3):(Int,Int))
  }

  it should "convert YES/NO element into a (bit value, bit width) pair" in {
    val encodedValues1 = InterviewBitSplitter.toBitPair(List("Yes"), PersonalServiceCluster.clusterElements(1))
    val encodedValues2 = InterviewBitSplitter.toBitPair(List("No"), PersonalServiceCluster.clusterElements(1))
    encodedValues1 shouldBe ((2,2):(Int,Int))
    encodedValues2 shouldBe ((1,2):(Int,Int))
  }

  it should "convert multiple element into a (bit value, bit width) pair" in {
    val encodedValues = InterviewBitSplitter.toBitPair(List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole"), FinancialRiskCluster.clusterElements(0))
    encodedValues shouldBe ((0x11,5):(Int,Int))
  }

}
