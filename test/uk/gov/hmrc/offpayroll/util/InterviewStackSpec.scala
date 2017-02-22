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
import uk.gov.hmrc.offpayroll.models._

class InterviewStackSpec extends FlatSpec with Matchers {

  private val setupElement = SetupCluster.clusterElements(2)
  private val exitElement = ExitCluster.clusterElements(0)
  private val firstElement = PersonalServiceCluster.clusterElements(0)
  private val middleElement = FinancialRiskCluster.clusterElements(0)
  private val lastElement = PartAndParcelCluster.clusterElements(3)
  private val setupElementValue = List("setup.provideServices.soleTrader")
  private val exitElementValue = List("No")
  private val firstElementValue = List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
  private val middleElementValue = List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole")
  private val lastElementValue = List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")

  "interview stack" should "push correctly one element value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "push correctly one element value for the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3))
  }

  it should "pop a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
    val (newStack, v) = InterviewStack.pop(stack, firstElement)
    v should contain theSameElementsInOrderAs List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "pop a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3))
    val (newStack, v) = InterviewStack.pop(stack, lastElement)
    v should contain theSameElementsInOrderAs List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "peek a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (3, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, firstElement)
    v should contain theSameElementsInOrderAs List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "peek a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (3, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, lastElement)
    v should contain theSameElementsInOrderAs List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "push, peek and pop correctly a middle element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), middleElementValue, middleElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (17, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, middleElement)
    v should contain theSameElementsInOrderAs List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack2, v2) = InterviewStack.pop(stack, middleElement)
    v2 should contain theSameElementsInOrderAs List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole")
    newStack2.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "push and peek a setup element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), setupElementValue, setupElement)
    val pairs = List((0,3), (0,2), (4,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, setupElement)
    v should contain theSameElementsInOrderAs List("setup.provideServices.soleTrader")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "push and peek an exit element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), exitElementValue, exitElement)
    val pairs = List((0,3), (0,2), (0,3), (1,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, exitElement)
    v should contain theSameElementsInOrderAs List("No")
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

}

