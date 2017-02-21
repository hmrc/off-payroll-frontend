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
import play.api.mvc.Session
import uk.gov.hmrc.offpayroll.models.{FinancialRiskCluster, PartAndParcelCluster, PersonalServiceCluster}
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.INTERVIEW_KEY

/**
  * Created by peter on 26/01/2017.
  */
class InterviewSessionStackSpec extends FlatSpec with Matchers {

  val mockSession = Session.deserialize(Map())
  private val firstElement = PersonalServiceCluster.clusterElements(0)
  private val middleElement = FinancialRiskCluster.clusterElements(0)
  private val lastElement = PartAndParcelCluster.clusterElements(3)
  private val firstElementValue = List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
  private val middleElementValue = List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole")
  private val lastElementValue = List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")


  "InterviewSessionStack" should "insert new interwiew if it is not present" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
  }

  "InterviewSessionStack" should "pop a value" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    val (poppedSession, values) = InterviewSessionStack.pop(newSession, firstElement)
    poppedSession(INTERVIEW_KEY) shouldBe "0"
    values should contain theSameElementsInOrderAs List("personalService.workerSentActualSubstitute.noSubstitutionHappened")
  }

  "InterviewSessionStack" should "push two values and pop a value" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), lastElementValue, lastElement)
    val (poppedSession, values) = InterviewSessionStack.pop(newSession, lastElement)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    values should contain theSameElementsInOrderAs List("partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
  }

  "InterviewSessionStack" should "push two values and peek a value" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement)
    val (poppedSession, values) = InterviewSessionStack.peek(newSession, middleElement)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UwYMK"
    values should contain theSameElementsInOrderAs List("financialRisk.workerProvidedMaterials", "financialRisk.expensesAreNotRelevantForRole")
  }

}
