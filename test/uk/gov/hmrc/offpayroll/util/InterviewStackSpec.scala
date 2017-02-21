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

  "interview stack" should "push correctly one element value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), List("personalService.workerSentActualSubstitute.noSubstitutionHappened"), PersonalServiceCluster.clusterElements(0))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
  }

  it should "push correctly one element value for the last element" in {
    val stack = InterviewStack.push(CompressedInterview(0L), List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness"), PartAndParcelCluster.clusterElements(3))
    println(stack.asValueWidthPairs)
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (3,3)))
  }

  it should "pop a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), List("personalService.workerSentActualSubstitute.noSubstitutionHappened"), PersonalServiceCluster.clusterElements(0))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs(List((3,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
    val (newStack, v) = InterviewStack.pop(stack, PersonalServiceCluster.clusterElements(0))
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs(List((0,3), (0,2), (0,2), (0,2), (0,2), (0,3), (0,3), (0,3), (0,3), (0,5), (0,3), (0,3), (0,2), (0,2), (0,2), (0,3)))
  }

  it should "peek a value" in {
    val stack = InterviewStack.push(CompressedInterview(0L), List("personalService.workerSentActualSubstitute.noSubstitutionHappened"), PersonalServiceCluster.clusterElements(0))
    val pairs = List((3, 3), (0, 2), (0, 2), (0, 2), (0, 2), (0, 3), (0, 3), (0, 3), (0, 3), (0, 5), (0, 3), (0, 3), (0, 2), (0, 2), (0, 2), (0, 3))
    stack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
    val (newStack, v) = InterviewStack.peek(stack, PersonalServiceCluster.clusterElements(0))
    v shouldBe 3
    newStack.asValueWidthPairs should contain theSameElementsInOrderAs pairs
  }

}

// add test for last element

