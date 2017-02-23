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

class InterviewSessionStackSpec extends FlatSpec with Matchers {
  val mockSession = Session.deserialize(Map())
  private val firstElement = PersonalServiceCluster.clusterElements(0)
  private val middleElement = FinancialRiskCluster.clusterElements(0)
  private val lastElement = PartAndParcelCluster.clusterElements(3)
  private val firstElementValue = "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  private val middleElementValue = "financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
  private val lastElementValue = "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"


  "InterviewSessionStack" should "insert new interwiew if it is not present" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
  }

  it should "pop a value" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    val (poppedSession, value) = InterviewSessionStack.pop(newSession, firstElement)
    poppedSession(INTERVIEW_KEY) shouldBe "0"
    value shouldBe "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  }

  it should "push two values and pop a value" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), lastElementValue, lastElement)
    val (poppedSession, value) = InterviewSessionStack.pop(newSession, lastElement)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    value shouldBe "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"
  }

  it should "push two values and peek a value" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement)
    val (poppedSession, value) = InterviewSessionStack.peek(newSession, middleElement)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UwYMK"
    value shouldBe "|financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
  }

  it should "push two values and pop the first value wiping out the stack" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), lastElementValue, lastElement)
    val (poppedSession, value) = InterviewSessionStack.pop(newSession, firstElement)
    poppedSession(INTERVIEW_KEY) shouldBe "0"
    value shouldBe "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  }

  it should "reset the interview" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    val resetSession = InterviewSessionStack.reset(newSession)
    resetSession.data.keys should not contain(INTERVIEW_KEY)
  }

  it should "reset the interview and pop correctly" in {
    val resetSession = InterviewSessionStack.reset(mockSession)
    resetSession.data.keys should not contain(INTERVIEW_KEY)
    val (poppedSession, value) = InterviewSessionStack.pop(resetSession, lastElement)
    poppedSession.data.keys should contain(INTERVIEW_KEY)
    value.isEmpty shouldBe true
    poppedSession(INTERVIEW_KEY) shouldBe "0"
  }

  it should "provide asMap function" in {
    val newSession = InterviewSessionStack.push(
      InterviewSessionStack.push(
        InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement),
      lastElementValue, lastElement
    )
    val map = InterviewSessionStack.asMap(newSession)
    val (maybeFirstValue, maybeMiddleValue, maybeLastValue) = (map.get(firstElement.questionTag), map.get(middleElement.questionTag), map.get(lastElement.questionTag))
    maybeFirstValue.get shouldBe "personalService.workerSentActualSubstitute.noSubstitutionHappened"
    maybeMiddleValue.get shouldBe "|financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
    maybeLastValue.get shouldBe "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"
  }

}
