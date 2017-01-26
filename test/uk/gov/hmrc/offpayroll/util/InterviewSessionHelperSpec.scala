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
import uk.gov.hmrc.offpayroll.util.InterviewSessionHelper.INTERVIEW_KEY

/**
  * Created by peter on 26/01/2017.
  */
class InterviewSessionHelperSpec extends FlatSpec with Matchers {

  val mockSession = Session.deserialize(Map())

  "An InterviewSessionHelper " should "take an existing Session and add a new value on an interview " in {
    val newSession = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    newSession.data.keys should contain(INTERVIEW_KEY)
  }

  val someInterview = "someQuestionTag:someAnswer;someOtherQuestionTag:someOtherAnswer"

  it should "be able to add a second value" in {
    val newSession  = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.addValue(newSession, "someOtherQuestionTag", "someOtherAnswer")
    finalSession.data.keys should contain(INTERVIEW_KEY)
    finalSession(INTERVIEW_KEY) shouldBe someInterview
  }

  it should "replace the value of an existing key" in {
    val newSession = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    val finalSession = InterviewSessionHelper.addValue(newSession, "someQuestionTag", "someOtherAnswer")
    finalSession(INTERVIEW_KEY) shouldBe "someQuestionTag:someOtherAnswer"
  }

  it should "convert a string encoded interview to a Map of String String" in {
    val newSession = InterviewSessionHelper.addValue(mockSession, "someQuestionTag", "someAnswer")
    InterviewSessionHelper.asMap(newSession) shouldBe Map(("someQuestionTag" -> "someAnswer" ))
  }


}
