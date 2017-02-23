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
  private val setupElementValue = "setup.provideServices.soleTrader"
  private val exitElementValue = "No"
  private val firstElementValue = "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  private val middleElementValue = "|financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
  private val lastElementValue = "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"

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
    val (newStack, t) = InterviewStack.pop(stack)
    t shouldBe "personalService.workerSentActualSubstitute"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "pop a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3))
    val (newStack, t) = InterviewStack.pop(stack)
    t shouldBe "partParcel.workerRepresentsEngagerBusiness"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "peek a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (3, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, t) = InterviewStack.peek(stack)
    t shouldBe "personalService.workerSentActualSubstitute"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "peek a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (3, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, t) = InterviewStack.peek(stack)
    t shouldBe "partParcel.workerRepresentsEngagerBusiness"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "push, peek and pop correctly a middle element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), middleElementValue, middleElement)
    val pairs = List((0,3), (0,2), (0,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (17, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, t) = InterviewStack.peek(stack)
    t shouldBe "financialRisk.haveToPayButCannotClaim"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack2, t2) = InterviewStack.pop(stack)
    t2 shouldBe "financialRisk.haveToPayButCannotClaim"
    newStack2.asValueWidthPairs should contain theSameElementsInOrderAs List((0,3), (0,2), (0,3), (0,2), (0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3))
  }

  it should "push and peek a setup element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), setupElementValue, setupElement)
    val pairs = List((0,3), (0,2), (4,3), (0,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, t) = InterviewStack.peek(stack)
    t shouldBe "setup.provideServices"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "push and peek an exit element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), exitElementValue, exitElement)
    val pairs = List((0,3), (0,2), (0,3), (1,2), (0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, t) = InterviewStack.peek(stack)
    t shouldBe "exit.officeHolder"
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

}

