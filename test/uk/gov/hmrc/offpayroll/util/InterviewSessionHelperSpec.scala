/*
 * Copyright 2019 HM Revenue & Customs
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
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.INTERVIEW_KEY

/**
  * Created by peter on 26/01/2017.
  */
class InterviewSessionHelperSpec extends FlatSpec with Matchers {

  val mockSession = Session.deserialize(Map())

  "An InterviewSessionHelper " should "take an existing Session and add a new value on an interview " in {
    val newSession = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    newSession.data.keys should contain(INTERVIEW_KEY)
  }

  val someInterview = "someQuestionTag:someAnswer;someOtherQuestionTag:someOtherAnswer"

  it should "be able to add a second value" in {
    val newSession  = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.push(newSession, "someOtherQuestionTag", "someOtherAnswer")
    finalSession.data.keys should contain(INTERVIEW_KEY)
    finalSession(INTERVIEW_KEY) shouldBe someInterview
  }

  it should "replace the value of an existing key" in {
    val newSession = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.push(newSession, "someQuestionTag", "someOtherAnswer")

    finalSession(INTERVIEW_KEY) shouldBe "someQuestionTag:someOtherAnswer"
  }

  it should "convert a string encoded interview to a Map of String String" in {
    val newSession = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    InterviewSessionHelper.asMap(newSession) shouldBe Map("someQuestionTag" -> "someAnswer" )
  }

  it should "pop the last value from the interview and return a new session without the last value" in {
    val newSession = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.push(newSession, "someOtherQuestionTag", "someOtherAnswer")

    val (updatedSession, questionTag) = InterviewSessionHelper.pop(finalSession)

    questionTag shouldBe "someOtherQuestionTag"
    InterviewSessionHelper.asMap(updatedSession) shouldBe Map("someQuestionTag" -> "someAnswer" )
  }

  it should "pop should work on an empty interview" in {
    val (updatedSession, questionTag) = InterviewSessionHelper.pop(mockSession)
    questionTag shouldBe ""
    InterviewSessionHelper.asMap(updatedSession) shouldBe Map()
  }

  it should "reset an interview which is not empty" in {
    val newSession = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    InterviewSessionHelper.asMap(newSession) shouldBe Map("someQuestionTag" -> "someAnswer" )
    val resetSession = InterviewSessionHelper.reset(newSession)
    InterviewSessionHelper.asMap(resetSession) shouldBe Map()
    val afterResetSession = InterviewSessionHelper.push(resetSession, "someQuestionTag", "someAnswer")
    InterviewSessionHelper.asMap(afterResetSession) shouldBe Map("someQuestionTag" -> "someAnswer" )
  }

  it should "reset an empty interview" in {
    val resetSession = InterviewSessionHelper.reset(mockSession)
    InterviewSessionHelper.asMap(resetSession) shouldBe Map()
  }

  it should "provide unchanged session and top element of a non empty interview" in {
    val session1 = InterviewSessionHelper.push(mockSession, "someQuestionTag", "someAnswer")
    val session2 = InterviewSessionHelper.push(session1, "someQuestionTag2", "someAnswer2")
    InterviewSessionHelper.asMap(session2) shouldBe Map("someQuestionTag" -> "someAnswer", "someQuestionTag2" -> "someAnswer2")
    val (peekSession, topTag) = InterviewSessionHelper.peek(session2)
    InterviewSessionHelper.asMap(peekSession) shouldBe Map("someQuestionTag" -> "someAnswer", "someQuestionTag2" -> "someAnswer2")
    topTag shouldBe "someQuestionTag2"
  }

  it should "provide unchanged session and empty top element of an empty interview" in {
    val (peekSession, topTag) = InterviewSessionHelper.peek(mockSession)
    println(topTag)
    InterviewSessionHelper.asMap(peekSession) shouldBe Map()
    topTag shouldBe ""
  }

  it should "encode pushed question tag and decode popped question tag" in {
    val newSession1 = InterviewSessionHelper.push(mockSession, "partOfOrganisation.aaa", "answer1")
    val newSession2 = InterviewSessionHelper.push(newSession1, "financialRisk.bbb", "answer2")
    val newSession3 = InterviewSessionHelper.push(newSession2, "businessStructure.ccc", "answer3")
    val newSession4 = InterviewSessionHelper.push(newSession3, "personalService.ddd", "answer4")
    val newSession5 = InterviewSessionHelper.push(newSession4, "control.eee", "answer5")
    val newSession6 = InterviewSessionHelper.push(newSession5, "setup.fff", "answer6")
    val finalSession = InterviewSessionHelper.push(newSession6, "exit.ggg", "answer7")
    InterviewSessionHelper.asMap(finalSession) shouldBe Map(
      "partOfOrganisation.aaa" -> "answer1",
      "financialRisk.bbb" -> "answer2",
      "businessStructure.ccc" -> "answer3",
      "personalService.ddd" -> "answer4",
      "control.eee" -> "answer5",
      "setup.fff" -> "answer6",
      "exit.ggg" -> "answer7"
    )

    val (updatedSession, questionTag) = InterviewSessionHelper.pop(finalSession)

    questionTag shouldBe "exit.ggg"
    InterviewSessionHelper.asMap(updatedSession) shouldBe Map(
      "partOfOrganisation.aaa" -> "answer1",
      "financialRisk.bbb" -> "answer2",
      "businessStructure.ccc" -> "answer3",
      "personalService.ddd" -> "answer4",
      "control.eee" -> "answer5",
      "setup.fff" -> "answer6"
    )
  }

}
