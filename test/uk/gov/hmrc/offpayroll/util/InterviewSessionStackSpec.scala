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
import uk.gov.hmrc.offpayroll.models.{ExitCluster, FinancialRiskCluster, PartAndParcelCluster, PersonalServiceCluster}
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.INTERVIEW_KEY

class InterviewSessionStackSpec extends FlatSpec with Matchers {
  val mockSession = Session.deserialize(Map())
  private val exitElement = ExitCluster.clusterElements(0)
  private val firstElement = PersonalServiceCluster.clusterElements(0)
  private val middleElement = FinancialRiskCluster.clusterElements(0)
  private val lastElement = PartAndParcelCluster.clusterElements(3)
  private val exitElementValue = "Yes"
  private val firstElementValue = "personalService.workerSentActualSubstitute.noSubstitutionHappened"
  private val middleElementValue = "financialRisk.workerProvidedMaterials|financialRisk.expensesAreNotRelevantForRole"
  private val lastElementValue = "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"


  "InterviewSessionStack" should "insert new interwiew if it is not present" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
  }

  it should "pop a question tag" in {
    val newSession = InterviewSessionStack.push(mockSession, firstElementValue, firstElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    val (poppedSession, questionTag) = InterviewSessionStack.pop(newSession)
    poppedSession(INTERVIEW_KEY) shouldBe "0"
    questionTag shouldBe "personalService.workerSentActualSubstitute"
  }

  it should "pop a question tag for exit element" in {
    val newSession = InterviewSessionStack.push(mockSession, exitElementValue, exitElement)
    newSession.data.keys should contain(INTERVIEW_KEY)
    newSession(INTERVIEW_KEY) shouldBe "4ziepp2G"
    val (poppedSession, questionTag) = InterviewSessionStack.pop(newSession)
    poppedSession(INTERVIEW_KEY) shouldBe "0"
    questionTag shouldBe "exit.officeHolder"
  }

  it should "push two values and pop a question tag" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), lastElementValue, lastElement)
    val (poppedSession, questionTag) = InterviewSessionStack.pop(newSession)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UuDRY"
    questionTag shouldBe "partParcel.workerRepresentsEngagerBusiness"
  }

  it should "push two values and peek a question tag" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement)
    val (poppedSession, questionTag) = InterviewSessionStack.peek(newSession)
    poppedSession(INTERVIEW_KEY) shouldBe "w4UwYMK"
    questionTag shouldBe "financialRisk.haveToPayButCannotClaim"
  }

  it should "push two values and pop two question tags wiping out the stack" in {
    val newSession = InterviewSessionStack.push(InterviewSessionStack.push(mockSession, firstElementValue, firstElement), lastElementValue, lastElement)
    val (poppedSession, _) = InterviewSessionStack.pop(newSession)
    val (poppedSession2, questionTag) = InterviewSessionStack.pop(poppedSession)
    poppedSession2(INTERVIEW_KEY) shouldBe "0"
    questionTag shouldBe "personalService.workerSentActualSubstitute"
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
    val (poppedSession, questinoTag) = InterviewSessionStack.pop(resetSession)
    poppedSession.data.keys should contain(INTERVIEW_KEY)
    questinoTag.isEmpty shouldBe true
    poppedSession(INTERVIEW_KEY) shouldBe ""
  }

  it should "provide asMap function" in {
    val newSession = InterviewSessionStack.push(
      InterviewSessionStack.push(
        InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement),
      lastElementValue, lastElement
    )
    val map = InterviewSessionStack.asMap(newSession)
    val (maybeFirstValue, maybeLastValue) = (map.get(firstElement.questionTag), map.get(lastElement.questionTag))
    maybeFirstValue.get shouldBe "personalService.workerSentActualSubstitute.noSubstitutionHappened"
    maybeLastValue.get shouldBe "partParcel.workerRepresentsEngagerBusiness.workAsBusiness"
    map("financialRisk.workerProvidedMaterials") shouldBe "Yes"
    map("financialRisk.expensesAreNotRelevantForRole") shouldBe "Yes"
  }

  it should "provide asList function" in {
    val newSession = InterviewSessionStack.push(
      InterviewSessionStack.push(
        InterviewSessionStack.push(mockSession, firstElementValue, firstElement), middleElementValue, middleElement),
      lastElementValue, lastElement
    )
    val list = InterviewSessionStack.asList(newSession)
    def find(questionTag: String) = list.find { case (t, _) => t == questionTag }
    val (maybeFirstValue, maybeLastValue) = (find(firstElement.questionTag), find(lastElement.questionTag))
    maybeFirstValue.get shouldBe ("personalService.workerSentActualSubstitute", "personalService.workerSentActualSubstitute.noSubstitutionHappened")
    maybeLastValue.get shouldBe ("partParcel.workerRepresentsEngagerBusiness", "partParcel.workerRepresentsEngagerBusiness.workAsBusiness")
    list should contain (("financialRisk.workerProvidedMaterials", "Yes"))
    list should contain (("financialRisk.expensesAreNotRelevantForRole", "Yes"))
  }

}
