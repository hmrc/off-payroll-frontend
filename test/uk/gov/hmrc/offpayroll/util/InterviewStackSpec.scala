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
import uk.gov.hmrc.offpayroll.models.{PartAndParcelCluster, PersonalServiceCluster}

class InterviewStackSpec extends FlatSpec with Matchers {

  private val firstElement = PersonalServiceCluster.clusterElements(0)
  private val lastElement = PartAndParcelCluster.clusterElements(3)
  private val firstElementValue = List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
  private val lastElementValue = List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")

  "interview stack" should "push correctly one element value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
  }

  it should "push correctly one element value for the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3)))
  }

  it should "pop a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
    val (newStack, v) = InterviewStack.pop(stack, firstElement)
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
  }

  it should "pop a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3)))
    val (newStack, v) = InterviewStack.pop(stack, lastElement)
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
  }

  it should "peek a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), firstElementValue, firstElement)
    val pairs = List((3, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, firstElement)
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

  it should "peek a value on the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), lastElementValue, lastElement)
    val pairs = List((0, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (3, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, lastElement)
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

}

